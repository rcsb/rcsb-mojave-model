package org.rcsb.mojave.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.rcsb.mojave.model.constants.ModuleConstants;
import org.rcsb.mojave.tools.utils.CommonUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Schema Resolver contains all logic necessary to fetch requested Schema from appropriate registry.
 *
 * Created on 5/11/18.
 *
 * @author Yana Valasatava
 * @since 1.0.0
 */
public class SchemaResolver {

    private final Properties props;
    private ObjectMapper mapper;

    public SchemaResolver() throws IOException {
        this.props = CommonUtils.getProjectProperties(ModuleConstants.PROPERTIES_RESOURCE_NAME);
    }

    public SchemaResolver setMapper(ObjectMapper m) {
        this.mapper = m;
        return this;
    }

    private InputStream getResourceAsInputStream(String name) {
        if(name.contains("embedding"))
            System.out.println("Empty resource name");
        return getClass().getResourceAsStream("/"+name);
    }

    private InputStream getSchemaInputStream(SchemaRegistry registryItem) {

        return getResourceAsInputStream(props.getProperty(registryItem.value()));
    }

    private String getSchema(InputStream inputStream) throws IOException {
        if (mapper == null)
            mapper = new ObjectMapper();

        Object o;
        try {
            o = mapper.readValue(inputStream, Object.class);
        } catch (IOException ioe) {
            throw new IOException("Unable to unmarshal JSON to an Object: ", ioe);
        }

        return mapper.writeValueAsString(o);
    }

    /**
     * Get JSON schema.
     *
     * @param path to the schema.
     * @return JSON schema as a String.
     */
    public String getSchema(Path path) throws IOException {

        FileInputStream inputStream = FileUtils.openInputStream(path.toFile());
        return getSchema(inputStream);
    }

    public String getSchema(SchemaRegistry view) throws IOException {
        return getSchema(getSchemaInputStream(view));
    }

    public String getSchemaVersion() {
        return props.getProperty(ModuleConstants.PROPERTIES_JSON_SCHEMA_VERSION);
    }
}
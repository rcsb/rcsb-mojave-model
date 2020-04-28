import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.rcsb.mojave.meta.enums.TypeEnum;
import org.rcsb.mojave.model.constants.ModuleConstants;
import org.rcsb.mojave.tools.jsonschema.SchemaLoader;
import org.rcsb.mojave.tools.jsonschema.model.JsonSchemaInstance;
import org.rcsb.mojave.tools.utils.CommonUtils;
import org.rcsb.mojave.tools.utils.ConfigurableMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * Created on 3/1/19.
 *
 * @author Yana Valasatava
 * @since 4.0.0
 */
public class TestTypeDuplicates {

    private void checkEqualType(String name, JsonSchemaInstance registered, JsonSchemaInstance duplicated) {

        if (registered.getType().equals(TypeEnum.ARRAY.value())
                && duplicated.getType().equals(TypeEnum.ARRAY.value())) {
            checkEqualType(name, registered.getItems(), duplicated.getItems());
        }

        else if (registered.getType().equals(TypeEnum.OBJECT.value()) ) {
            if (!registered.equals(duplicated))
                throw new RuntimeException("Different definitions have the same name.");
            else
                System.out.println("Type '"+name+"' has a duplicate");
        }
    }

    @Test
    public void shouldBeSameTypeForNameDuplicates() throws IOException {

        Properties p = CommonUtils.getProjectProperties(ModuleConstants.PROPERTIES_RESOURCE_NAME);

        if (!p.containsKey("core.schema.location"))
            throw new RuntimeException("Core schemas location is not defined.");

        String location = p.getProperty("core.schema.location");
        File directory = new File("target/" + location);
        if (!directory.exists())
            throw new RuntimeException("Core schemas location does not exist.");

        List<String> errors = new ArrayList<>();

        Map<String, JsonSchemaInstance> registeredTypes = new HashMap<>();

        SchemaLoader loader = new SchemaLoader();
        for (File f : Objects.requireNonNull(directory.listFiles())) {

            JsonNode json = loader.readSchema(new URL("file://"+f.getAbsolutePath()));

            JsonSchemaInstance schema = ConfigurableMapper.getMapper().convertValue(json, JsonSchemaInstance.class);

            for (Map.Entry<String, JsonSchemaInstance> e : schema.getProperties().entrySet()) {

                String name = e.getKey();
                if (!registeredTypes.containsKey(name)) {
                    registeredTypes.put(name, e.getValue());
                } else {
                    JsonSchemaInstance registered = registeredTypes.get(name);
                    JsonSchemaInstance duplicated = e.getValue();
                    try {
                        checkEqualType(name, registered, duplicated);
                    } catch (Exception ex) {
                        String msg = ex.getMessage();
                        msg += String.format(" Type %s has different definitions. Second duplicate is found in %s",
                                name, f.getName());
                        System.out.println(msg);
                        errors.add(msg);
                    }
                }
            }
        }
        assertTrue(errors.isEmpty());
    }
}

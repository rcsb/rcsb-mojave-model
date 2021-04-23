import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import validation.model.ValidationContext;
import validation.visitor.CompositeSchemaContextCollector;
import validation.visitor.SchemaMetadataValidator;
import org.rcsb.mojave.tools.jsonschema.SchemaLoader;
import org.rcsb.mojave.tools.jsonschema.traversal.JsonSchemaWalker;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created on 4/1/20.
 *
 * @author Yana Valasatava
 * @since 4.0.0
 */
public class TestSchemaMetadata {

    private static final Logger logger = LoggerFactory.getLogger(TestSchemaMetadata.class);

    private static void reportFailures(Map<ValidationContext.FailureType, Set<String>> failures) {

        if (failures.containsKey(ValidationContext.FailureType.EMPTY)) {
            System.out.println("[WARN] Nested array has 0 search fields");
            for (String name : failures.get(ValidationContext.FailureType.EMPTY))
                System.out.println("[WARN] Fields name: "+name);
        } else if (failures.containsKey(ValidationContext.FailureType.SINGLE_FIELD)) {
            System.out.println("[WARN] Nested array has only 1 search field");
            for (String name : failures.get(ValidationContext.FailureType.SINGLE_FIELD))
                System.out.println("[WARN] Fields name: "+name);
        }
    }

    @Test
    public void shouldValidateIndexingMetadata() throws IOException {

        URL url = getClass().getResource("");
        if (url == null) throw new IOException("Cannot read schema files");
        String path = url.getPath();
        //apply a filter to fetch core schemas only
        File[] schemaFiles = new File(path).listFiles((dir, name) ->
                name.startsWith("core_") && name.endsWith(".json"));
        assert schemaFiles != null;

        SchemaLoader loader = new SchemaLoader();
        CompositeSchemaContextCollector collector = new CompositeSchemaContextCollector();

        for(File f : schemaFiles) {

            SchemaMetadataValidator validator = new SchemaMetadataValidator();
            validator.setSchemaName(f.getName());

            JsonNode schema = loader.readSchema(f.toURI());
            JsonSchemaWalker walker = new JsonSchemaWalker.Builder()
                    .fromInstance(schema)
                    .acceptingVisitors(asList(validator, collector))
                    .build();
            walker.walk();
            if (!validator.getValidationCtx().isSuccess()) {
                logger.error("Issues with indexing metadata in the schema file: " + f.getName());
                reportFailures(validator.getValidationCtx().getFailures());
                throw new IllegalStateException("Failed to validate indexing metadata");
            }
        }

        if (collector.getCollector().size() > 0) {
            for (String fieldName : collector.getCollector()) {
                logger.error("Search metadata cannot be used for composite types. " +
                        "Field name: " + fieldName);
            }
            throw new IllegalStateException("Failed to validate indexing metadata");
        }
    }

    @Test
    public void shouldCollectFieldsWithCompositeSchemaSearchContext() throws URISyntaxException, IOException {
        String resourcePath = "/schema/json-schema-composite.json";
        URL url = TestSchemaMetadata.class.getResource(resourcePath);
        if (url == null)
            throw new IllegalStateException("Test schema file [ "+resourcePath+" ] does not exist.");

        CompositeSchemaContextCollector visitor = new CompositeSchemaContextCollector();
        SchemaLoader loader = new SchemaLoader();
        JsonNode schema = loader.readSchema(url.toURI());
        JsonSchemaWalker walker = new JsonSchemaWalker.Builder()
                .fromInstance(schema)
                .acceptingVisitors(singletonList(visitor))
                .build();
        walker.walk();

        Set<String> errors = visitor.getCollector();
        assertEquals(2, errors.size());
        assertTrue(errors.contains("field4") && errors.contains("field5"));
    }
}

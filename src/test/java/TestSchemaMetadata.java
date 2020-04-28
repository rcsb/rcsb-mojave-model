import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import validation.model.ValidationContext;
import validation.visitor.SchemaMetadataValidator;
import org.rcsb.mojave.tools.jsonschema.SchemaLoader;
import org.rcsb.mojave.tools.jsonschema.traversal.JsonSchemaWalker;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.singletonList;

/**
 * Created on 4/1/20.
 *
 * @author Yana Valasatava
 * @since 4.0.0
 */
public class TestSchemaMetadata {

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

        for(File f : schemaFiles) {

            SchemaMetadataValidator validator = new SchemaMetadataValidator();
            validator.setSchemaName(f.getName());

            JsonNode schema = loader.readSchema(f.toURI());
            JsonSchemaWalker walker = new JsonSchemaWalker.Builder()
                    .fromInstance(schema)
                    .acceptingVisitors(singletonList(validator))
                    .build();
            walker.walk();
            if (!validator.getValidationCtx().isSuccess()) {
                System.out.println(" ");
                System.out.println("[WARN] Issues with indexing metadata in the schema file: " + f.getName());
                reportFailures(validator.getValidationCtx().getFailures());
                throw new IllegalStateException("Failed to validate indexing metadata");
            }
        }
    }
}

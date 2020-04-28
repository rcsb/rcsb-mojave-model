import org.junit.Test;
import org.rcsb.mojave.model.SchemaResolver;
import org.rcsb.mojave.model.SchemaRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created on 4/17/19.
 *
 * @author Yana Valasatava
 * @since 4.0.0
 */
public class TestSchemaProvider {

    @Test
    public void shouldProvideSchemasDeclaredInSchemaView() throws IOException {

        SchemaResolver provider = new SchemaResolver();

        String schema;
        List<String> errors = new ArrayList<>();
        for ( SchemaRegistry v : SchemaRegistry.values()) {
            try {
                schema = provider.getSchema(v);
                assertFalse(schema.isEmpty());
            } catch (Exception ex) {
                String msg = ex.getMessage();
                msg += String.format(" %s schema is not available. Check if property %s is defined in pom.xml.",
                        v.name(), v.value());
                System.out.println(msg);
                errors.add(msg);
            }
        }
        assertTrue(errors.isEmpty());
    }
}

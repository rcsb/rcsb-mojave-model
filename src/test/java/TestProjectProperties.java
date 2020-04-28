import org.junit.Test;
import org.rcsb.mojave.model.ModuleConstants;
import org.rcsb.mojave.tools.utils.CommonUtils;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;

/**
 * Created on 5/23/19.
 *
 * @author Yana Valasatava
 * @since 4.0.0
 */
public class TestProjectProperties {

    @Test
    public void shouldGenerateProjectProperties() throws IOException {

        Properties p = CommonUtils.getProjectProperties(ModuleConstants.PROPERTIES_RESOURCE_NAME);
        assertNotNull(p);
    }

    @Test
    public void shouldContainSchemaVersionProperty() throws IOException {

        Properties p = CommonUtils.getProjectProperties(ModuleConstants.PROPERTIES_RESOURCE_NAME);
        assertNotNull(p.getProperty(ModuleConstants.PROPERTIES_JSON_SCHEMA_VERSION));

        System.out.println("============================================================");
        System.out.println("Successfully retrieved schema version property: "
                +p.getProperty(ModuleConstants.PROPERTIES_JSON_SCHEMA_VERSION));
        System.out.println("============================================================");
    }
}

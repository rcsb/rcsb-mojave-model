import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * A test to check for duplicate class names in the automatically generated classes.
 * <p>
 * This can be useful to make sure that we don't have duplicate class names in schema for
 * use cases like graphql that have a single namespace and don't admit duplicates.
 *
 * @author Jose Duarte
 * @since 4.0.0
 */
public class TestDuplicateJavaTypeNames {

    @Test
    public void testDuplicateTypeNames() {

        Set<Class<?>> allClasses = findClasses("org.rcsb.mojave.auto");

        List<Class<?>[]> duplicates = new ArrayList<>();

        Map<String, Class<?>> uniqueNames = new HashMap<>();

        for (Class<?> theclass : allClasses) {

            Class<?>[] pair = new Class[2];
            Class<?> present = uniqueNames.put(theclass.getSimpleName(), theclass);
            if (present!=null) {
                pair[0] = present;
                pair[1] = theclass;
                duplicates.add(pair);
            }
        }

        assertEquals(formatDupsMsg(duplicates), 0, duplicates.size());
    }

    /**
     * A wrapper around the reflections library to find
     * all classes in a certain package recursively.
     * <p>
     * It will not find anything that is not a class (e.g. interfaces).
     * It will find inner classes.
     *
     * @param packageName the package name e.g. "org.rcsb.mojave.auto"
     * @return a list of all classes
     */
    private static Set<Class<?>> findClasses(String packageName) {

        // see https://stackoverflow.com/questions/520328/can-you-find-all-classes-in-a-package-using-reflection
        Reflections reflections = new Reflections(new ConfigurationBuilder()

                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forPackage(packageName))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));

        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

        // adding inner classes by calling getDeclaredClasses
        Set<Class<?>> toAdd = new HashSet<>();

        for (Class<?> aClass : classes) {
            Class<?>[] innerClasses = aClass.getDeclaredClasses();
            Collections.addAll(toAdd, innerClasses);
        }

        classes.addAll(toAdd);

        return classes;
    }

    private static String formatDupsMsg(List<Class<?>[]> duplicates) {

        StringBuilder sb = new StringBuilder();
        if (duplicates.size()>0) {
            sb.append(duplicates.size()).append(" duplicate classes: \n");
        }
        for (Class<?>[] pair : duplicates) {
            sb.append(pair[0].getName());
            sb.append(" -- ");
            sb.append(pair[1].getName());
            sb.append("\n");
        }
        return sb.toString();
    }
}

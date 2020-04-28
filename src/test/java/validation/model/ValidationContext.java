package validation.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created on 12/10/19.
 *
 * @author Yana Valasatava
 * @since 1.0.0
 */
public class ValidationContext {

    private Map<FailureType, Set<String>> failures;

    public ValidationContext() {
        failures = new HashMap<>();
    }

    public boolean isSuccess() {
        return failures.isEmpty();
    }

    public Map<FailureType, Set<String>> getFailures() {
        return failures;
    }

    public void setFailures(Map<FailureType, Set<String>> failures) {
        this.failures = failures;
    }

    public enum FailureType {
        EMPTY,
        SINGLE_FIELD
    }
}

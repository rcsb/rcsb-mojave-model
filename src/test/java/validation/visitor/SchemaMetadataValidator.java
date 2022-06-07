package validation.visitor;


import com.fasterxml.jackson.databind.JsonNode;
import org.rcsb.mojave.meta.enums.*;
import org.rcsb.mojave.meta.MetaSchemaConstants;
import org.rcsb.mojave.tools.jsonschema.traversal.model.TraversalContext;
import org.rcsb.mojave.tools.jsonschema.traversal.visitables.Visitable;
import org.rcsb.mojave.tools.jsonschema.traversal.visitables.VisitableNode;
import org.rcsb.mojave.tools.jsonschema.traversal.visitors.Visitor;
import org.rcsb.mojave.tools.jsonschema.utils.JsonSchemaNodeUtils;
import validation.model.ValidationContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 11/19/19.
 *
 * @author Yana Valasatava
 * @since 1.0.0
 */
public class SchemaMetadataValidator implements Visitor {

    private String schemaName;
    private final ValidationContext validationCtx;

    public SchemaMetadataValidator() {
        validationCtx = new ValidationContext();
    }

    private String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public ValidationContext getValidationCtx() {
        return validationCtx;
    }

    private List<String> getSearchContext(JsonNode contextObj) {
        List<String> context = new ArrayList<>();
        contextObj.iterator().forEachRemaining(n -> context.add(n.textValue()));
        return context;
    }

    private void checkNumericField(String fieldName, List<String> context) {
        if (context.size() != 1 || !context.get(0).equals(RcsbSearchContextEnum.DEFAULT_MATCH.value()))
                throw new IllegalStateException("ERROR in metadata for "+fieldName
                        +". Search context for numeric field MUST only contain [ "
                        +RcsbSearchContextEnum.DEFAULT_MATCH.value()+ " ]");
    }

    private void checkTextField(String fieldName, List<String> context) {
        for (String c : context) {
            if (!c.equals(RcsbSearchContextEnum.FULL_TEXT.value())
                    && !c.equals(RcsbSearchContextEnum.EXACT_MATCH.value())
                    && !c.equals(RcsbSearchContextEnum.SUGGEST.value())) {
                throw new IllegalStateException("ERROR in metadata for [ "+fieldName+" ] (in schema '"+getSchemaName()+"'). "
                        +"Search context for text field CAN NOT contain [ "+c+" ]");
            }
        }
    }

    private void validateSearchContextByType(JsonNode node, String fieldName, List<String> context) {
        if (JsonSchemaNodeUtils.isDate(node) || JsonSchemaNodeUtils.isNumeric(node))
            checkNumericField(fieldName, context);
        else if (JsonSchemaNodeUtils.isString(node))
            checkTextField(fieldName, context);
        else if (JsonSchemaNodeUtils.isArray(node))
            validateSearchContextByType(node.get(MetaSchemaConstants.ITEMS), fieldName, context);
    }

    private void validateNestedHasSearchContext(AtomicInteger count, JsonNode node) {

        if (node.has(MetaSchemaConstants.RCSB_SEARCH_CONTEXT))
            count.incrementAndGet();

        if (JsonSchemaNodeUtils.isObject(node)) {
            JsonNode properties = node.get(MetaSchemaConstants.PROPERTIES);
            properties.fieldNames().forEachRemaining(name ->
                    validateNestedHasSearchContext(count, properties.get(name)));
        }
    }

    @Override
    public void visit(Visitable visitableNode) {

        if (!(visitableNode instanceof VisitableNode))
            throw new IllegalArgumentException("Node object MUST be an instance of VisitableNode.");

        JsonNode node = ((VisitableNode) visitableNode).getNode();
        TraversalContext ctx = ((VisitableNode) visitableNode).getTraversalContext();

        if (node.has(MetaSchemaConstants.RCSB_SEARCH_CONTEXT)) {
            List<String> context = getSearchContext(node.get(MetaSchemaConstants.RCSB_SEARCH_CONTEXT));
            validateSearchContextByType(node, ctx.getCurrentFieldName(), context);
        }

        if (node.has(MetaSchemaConstants.RCSB_NESTED_INDEXING)
                && node.get(MetaSchemaConstants.RCSB_NESTED_INDEXING).asBoolean()) {

            if (!node.has(MetaSchemaConstants.TYPE)
                    || !node.get(MetaSchemaConstants.TYPE).asText().equals(TypeEnum.ARRAY.value()))
                throw new IllegalStateException("ERROR in metadata for [ "+ctx.getFullyQualifiedName()
                        +" ] (in schema ' "+getSchemaName()+" '): '"+ MetaSchemaConstants.RCSB_NESTED_INDEXING
                        + "' metadata item MUST be a property of array");

            AtomicInteger count = new AtomicInteger(0);
            validateNestedHasSearchContext(count, node.get(MetaSchemaConstants.ITEMS));

            if ( count.get() >= 2 )
                return;

            ValidationContext.FailureType failureType = ValidationContext.FailureType.EMPTY;
            String fqFieldName =  ctx.getFullyQualifiedName();
            if (count.get() == 1)
                failureType = ValidationContext.FailureType.SINGLE_FIELD;

            validationCtx.getFailures().putIfAbsent(failureType, new HashSet<>());
            validationCtx.getFailures().get(failureType).add(fqFieldName);
        }
    }
}

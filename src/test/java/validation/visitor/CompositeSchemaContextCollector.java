package validation.visitor;

import com.fasterxml.jackson.databind.JsonNode;
import org.rcsb.mojave.meta.MetaSchemaConstants;
import org.rcsb.mojave.tools.jsonschema.constants.MetaSchemaProperty;
import org.rcsb.mojave.tools.jsonschema.traversal.model.TraversalContext;
import org.rcsb.mojave.tools.jsonschema.traversal.visitables.Visitable;
import org.rcsb.mojave.tools.jsonschema.traversal.visitables.VisitableNode;
import org.rcsb.mojave.tools.jsonschema.traversal.visitors.Visitor;
import org.rcsb.mojave.tools.jsonschema.utils.JsonSchemaNodeUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created on 4/23/21.
 *
 * @author Yana Rose
 * @since 1.0.0
 */
public class CompositeSchemaContextCollector implements Visitor {

    private Set<String> collector;

    public CompositeSchemaContextCollector() {
        collector = new HashSet<>();
    }

    public Set<String> getCollector() {
        return collector;
    }

    private Iterator<JsonNode> listCompositeSchemas(JsonNode node) {
        if (node.has(MetaSchemaProperty.ANY_OF))
            return node.get(MetaSchemaProperty.ANY_OF).iterator();
        else if (node.has(MetaSchemaProperty.ONE_OF))
            return node.get(MetaSchemaProperty.ONE_OF).iterator();
        else
            return node.get(MetaSchemaProperty.ALL_OF).iterator();
    }

    private boolean compositeHasSearchContext(JsonNode node) {

        if (node.has(MetaSchemaConstants.RCSB_SEARCH_CONTEXT))
            return true;

        if (JsonSchemaNodeUtils.isArray(node))
            node = node.get(MetaSchemaProperty.ITEMS);

        Iterator<JsonNode> nodes = listCompositeSchemas(node);
        while (nodes.hasNext()) {
            JsonNode n = nodes.next();
            if (n.has(MetaSchemaConstants.RCSB_SEARCH_CONTEXT))
                return true;
        }
        return false;
    }

    private boolean isComposite(JsonNode node) {
        if (JsonSchemaNodeUtils.isArray(node)
                && JsonSchemaNodeUtils.isComposite(node.get(MetaSchemaProperty.ITEMS))) {
            return true;
        } else
            return JsonSchemaNodeUtils.isComposite(node);
    }

    @Override
    public void visit(Visitable visitableNode) {

        if (!(visitableNode instanceof VisitableNode))
            throw new IllegalArgumentException("Node object MUST be an instance of VisitableNode.");

        JsonNode node = ((VisitableNode) visitableNode).getNode();
        TraversalContext ctx = ((VisitableNode) visitableNode).getTraversalContext();

        if (isComposite(node) && compositeHasSearchContext(node)) {
            String fqFieldName =  ctx.getFullyQualifiedName();
            collector.add(fqFieldName);
        }
    }
}

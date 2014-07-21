package genie.engine.parse.model;

import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import genie.engine.parse.modlan.Processor;
import genie.engine.parse.modlan.ProcessorRegistry;

/**
 * Created by midvorki on 3/22/14.
 */
public class ProcessorTree
        extends ProcessorNode implements ProcessorRegistry
{
    public static String DOC_ROOT_NAME = "doc-root";

    public ProcessorTree()
    {
        super("model", false);
    }

    public Processor getRoot()
    {
        return this;
    }

    public ParseNode getDocRoot()
    {
        return (ParseNode) getChild(DOC_ROOT_NAME);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItemOrNull)
    {
        return null; // RETURN NULL TO ASSUME DEFAULTS
    }

    public void endCB(Node aInData, Item aInItemOrNull)
    {

    }

}

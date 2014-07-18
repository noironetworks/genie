package genie.engine.parse.model;

import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ProcessorNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;

/**
 * Created by midvorki on 7/16/14.
 */
public class ParseNode extends ProcessorNode
{
    public ParseNode(String aInName)
    {
        this(aInName, false);
    }

    public ParseNode(String aInName, boolean aInIsRecursive)
    {
        super(aInName,aInIsRecursive);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        return null; // ASSUME DEFAULTS, AS NOTHING DONE
    }

    public void endCB(Node aInData, Item aInItem)
    {
        // DO NOTHING
    }
}

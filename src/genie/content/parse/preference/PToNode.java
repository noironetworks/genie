package genie.content.parse.preference;

import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;

/**
 * Created by midvorki on 8/1/14.
 */
public class PToNode extends ParseNode
{
    public PToNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        return null;
    }
}

package genie.content.parse.ploader;

import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;

/**
 * Created by midvorki on 7/25/14.
 */
public class PDomainNode extends ParseNode
{
    public PDomainNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective, Item> beginCB(Node aInData, Item aInParentItem)
    {
        // DO NOTHING
        return null;
    }
}
package genie.content.parse.ptype;

import genie.content.model.module.Module;
import genie.content.model.mtype.MType;
import genie.content.model.mtype.MTypeHint;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/21/14.
 */
public class PHintNode
        extends ParseNode
{
    /**
     * Constructor
     */
    public PHintNode(String aInName)
    {
        super(aInName, true);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        MTypeHint lHint = new MTypeHint((MType)aInParentItem);
        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE,lHint);
    }

}

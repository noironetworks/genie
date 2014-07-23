package genie.content.parse.pprop;

import genie.content.model.mclass.MClass;
import genie.content.model.mprop.MProp;
import genie.content.model.mprop.PropAction;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/23/14.
 */
public class PMemberNode
        extends ParseNode
{
    /**
     * Constructor
     */
    public PMemberNode(String aInName)
    {
        super(aInName);
        action = PropAction.get(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        String lName = aInData.getNamedValue(Strings.NAME,null, true);
        MProp lProp = new MProp((MClass)aInParentItem,lName,action);
        String lType = aInData.getNamedValue(Strings.TYPE,null,false);
        if (!Strings.isEmpty(lType))
        {
            lProp.addType(lType);
        }
        return null;
    }

    private final PropAction action;
}

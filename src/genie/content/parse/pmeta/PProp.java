package genie.content.parse.pmeta;

import genie.content.model.mmeta.MNode;
import genie.content.model.mmeta.MNodeProp;
import genie.engine.parse.model.ParseNodePropType;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/22/14.
 */
public class PProp
        extends ParseNode
{
    /**
     * Constructor
     */
    public PProp(String aInName)
    {
        super(aInName, false);
        type = ParseNodePropType.get(aInName);
    }

    /**
     * checks if the property is supported by this node. this overrides behavior to always return true
     * @param aInName name of the property
     * @return always returns true
     */
    public boolean hasProp(String aInName)
    {
        return true;
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        MNodeProp lNP = new MNodeProp((MNode)aInParentItem,aInData.getNamedValue(Strings.NAME, null, true), getType());
        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE, lNP);
    }

    public ParseNodePropType getType()
    {
        return type;
    }

    private ParseNodePropType type;

}

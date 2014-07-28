package genie.content.parse.ptype;

import genie.content.model.mtype.DefinedIn;
import genie.content.model.mtype.MConstants;
import genie.content.model.mtype.MConstraints;
import genie.content.model.mtype.MLanguageBinding;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/28/14.
 */
public class PConstraintsNode
        extends ParseNode
{
    /**
     * Constructor
     */
    public PConstraintsNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        return new Pair<ParseDirective, Item>(
                    ParseDirective.CONTINUE,
                    new MConstraints((MLanguageBinding) aInParentItem,
                                   aInData.getNamedValue("use-type", null, false),
                                   aInData.getNamedValue("min", null, false),
                                   aInData.getNamedValue("max", null, false),
                                   aInData.getNamedValue("default", null, false),
                                   Integer.parseInt(aInData.getNamedValue("default", "-1", false)),
                                   aInData.getNamedValue("regex", null, false)));
    }

}
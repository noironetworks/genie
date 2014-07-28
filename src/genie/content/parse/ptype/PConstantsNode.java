package genie.content.parse.ptype;

import genie.content.model.mtype.DefinedIn;
import genie.content.model.mtype.MConstants;
import genie.content.model.mtype.MLanguageBinding;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;

/**
 * Created by midvorki on 7/28/14.
 */
public class PConstantsNode
        extends ParseNode
{
    /**
     * Constructor
     */
    public PConstantsNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        return new Pair<ParseDirective, Item>(
            ParseDirective.CONTINUE,
            new MConstants((MLanguageBinding) aInParentItem,
                           DefinedIn.get(aInData.getNamedValue("defined-in", null, false)),
                           aInData.getNamedValue("prefix", null, false),
                           aInData.getNamedValue("suffix", null, false)));
    }

}
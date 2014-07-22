package genie.content.parse.ptype;

import genie.content.model.mtype.Language;
import genie.content.model.mtype.MLanguageBinding;
import genie.content.model.mtype.MType;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/21/14.
 */
public class PLanguageNode
        extends ParseNode
{
    /**
     * Constructor
     */
    public PLanguageNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        MLanguageBinding lLang = new MLanguageBinding(
                (MType)aInParentItem,
                Language.get(aInData.getNamedValue(Strings.NAME, null, true)));

        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE, lLang);
    }

}

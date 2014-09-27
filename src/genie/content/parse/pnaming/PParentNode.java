package genie.content.parse.pnaming;

import genie.content.model.mnaming.MNamer;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 8/1/14.
 */
public class PParentNode extends ParseNode
{
    public PParentNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        return new Pair<ParseDirective, Item>(
                            ParseDirective.CONTINUE,
                            ((MNamer)aInParentItem).getNameRule(
                                aInData.getNamedValue(
                                     Strings.CLASS,
                                     aInData.getNamedValue(Strings.NAME, null, false),
                                     true),
                                true));
    }
}

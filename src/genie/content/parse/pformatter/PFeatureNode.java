package genie.content.parse.pformatter;

import genie.content.model.mformatter.MFormatterDomain;
import genie.content.model.mformatter.MFormatterFeature;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/24/14.
 */
public class PFeatureNode extends ParseNode
{
    public PFeatureNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        MFormatterFeature lF = new MFormatterFeature(
                (MFormatterDomain) aInParentItem,
                aInData.getNamedValue(Strings.NAME,null, true));
        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE, lF);
    }
}

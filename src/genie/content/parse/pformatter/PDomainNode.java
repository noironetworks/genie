package genie.content.parse.pformatter;

import genie.content.model.mformatter.MFormatterDomain;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/24/14.
 */
public class PDomainNode extends ParseNode
{
    public PDomainNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        MFormatterDomain lDom = new MFormatterDomain(aInData.getNamedValue(Strings.NAME,null, true));
        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE,lDom);
    }
}

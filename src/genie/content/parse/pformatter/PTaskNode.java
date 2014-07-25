package genie.content.parse.pformatter;

import genie.content.model.mformatter.MFormatterFeature;
import genie.content.model.mformatter.MFormatterTask;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/24/14.
 */
public class PTaskNode extends ParseNode
{
    public PTaskNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        MFormatterTask lTask = new MFormatterTask(
                    (MFormatterFeature) aInParentItem,
                    aInData.getNamedValue(Strings.NAME,null, true));
        return null;
    }
}

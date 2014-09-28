package genie.content.parse.pnaming;

import genie.content.model.mcont.MContained;
import genie.content.model.mcont.MParent;
import genie.content.model.mnaming.MNameComponent;
import genie.content.model.mnaming.MNameRule;
import genie.content.model.mnaming.MNamer;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.model.ProcessorNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;

/**
 * Created by midvorki on 8/1/14.
 */
public class PComponentNode extends ParseNode
{
    public PComponentNode(String aInName)
    {
        super(aInName);
    }

    protected void addParent(ProcessorNode aInParent)
    {
        super.addParent(aInParent);
        isPartOfContainmentDef = aInParent instanceof genie.content.parse.pcontainment.PParentNode;
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        // CHECK IF THE NAMING RULE IS DEFINED AS PART OF CONTAINMENT,
        // IF IT IS, WE NEED TO ALLOCATE THE NAME RULE THAT SHADOWS THE CONTAINMENT RULE
        if (isPartOfContainmentDef)
        {
            MParent lParent = (MParent) aInParentItem;
            MContained lContd = lParent.getContained();
            MNamer lNamer = MNamer.get(lContd.getLID().getName(), true);
            MNameRule lNr = lNamer.getNameRule(lParent.getLID().getName(), true);
            aInParentItem = lNr;
        }
        return new Pair<ParseDirective, Item>(
                ParseDirective.CONTINUE,
                new MNameComponent((MNameRule)aInParentItem,
                                   aInData.getNamedValue("prefix", null, false),
                                   aInData.getNamedValue("member", null, false)
                                   ));
    }

    private boolean isPartOfContainmentDef = false;
}
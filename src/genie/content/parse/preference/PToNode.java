package genie.content.parse.preference;

import genie.content.model.mclass.MClass;
import genie.content.model.mcont.MContained;
import genie.content.model.mcont.MContainer;
import genie.content.model.mrelator.MRelated;
import genie.content.model.mrelator.MRelator;
import genie.content.model.mrelator.PointCardinality;
import genie.content.model.mrelator.RelatorType;
import genie.content.parse.pcontainment.PContainedNode;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 8/1/14.
 */
public class PToNode extends ParseNode
{
    public PToNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
//        PDependencyNode lContainedParseNode = (PDependencyNode) getParent();
        RelatorType lRelatorType = ((PDependencyNode) getParent()).getType();
        String lRelatorName = aInData.getParent().getNamedValue(Strings.NAME,"Reln",true);
        String lRelatingClassName = aInParentItem.getLID().getName();
        PointCardinality lRelatingCardinality =
                PointCardinality.get(aInData.getParent().getNamedValue(Strings.CARDINALITY, "many", true));

        PointCardinality lRelatedCardinality = PointCardinality.get(
                aInData.getNamedValue(Strings.CARDINALITY, "single", true));

        String lRelatedClassName =
                aInData.getNamedValue(Strings.CLASS, null, true);

        Pair<MRelator, MRelated> lRule = MRelator.addRule(
                lRelatorType, lRelatorName, lRelatingClassName,lRelatingCardinality, lRelatedClassName, lRelatedCardinality) ;

//        Severity.WARN.report(this.toString(),"","","CONT RULE ADDED: " + lRule);
        MRelated lRelated = lRule.getSecond();

        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE, lRelated);
    }
}

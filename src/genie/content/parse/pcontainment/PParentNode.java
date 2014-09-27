package genie.content.parse.pcontainment;

import genie.content.model.mclass.MClass;
import genie.content.model.mcont.MContained;
import genie.content.model.mcont.MContainer;
import genie.content.model.mcont.MParent;
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
        PContainedNode lContainedParseNode = (PContainedNode) getParent();

        String lContainedClassName = aInParentItem.getLID().getName();
        String lContainerClassName = lContainedParseNode.isRoot() ?
                                            MClass.ROOT_CLASS_GNAME :
                                            aInData.getNamedValue(Strings.CLASS, null, true);

        Pair<MContained, MParent> lRule = MContained.addRule(lContainerClassName,lContainedClassName);

//        Severity.WARN.report(this.toString(),"","","CONT RULE ADDED: " + lRule);
        MParent lContainer = lRule.getSecond();

        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE, lContainer);
    }
}

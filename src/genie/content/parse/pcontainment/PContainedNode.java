package genie.content.parse.pcontainment;

import genie.content.model.mclass.MClass;
import genie.content.model.mcont.DefinitionScope;
import genie.content.model.mcont.MContained;
import genie.content.model.mcont.MContainer;
import genie.content.model.mcont.MParent;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.model.ProcessorNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.report.Severity;
import modlan.utils.Strings;

/**
 * Created by midvorki on 8/1/14.
 */
public class PContainedNode extends ParseNode
{
    public PContainedNode(String aInName)
    {
        super(aInName);
        isRoot = Strings.ROOT.equalsIgnoreCase(aInName);
    }

    protected void addParent(ProcessorNode aInParent)
    {
        super.addParent(aInParent);
        scope = DefinitionScope.get(aInParent.getName());
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        String lContainedClass = DefinitionScope.CLASS == scope ?
                                            aInParentItem.getGID().getName() :
                                            aInData.getNamedValue(Strings.CLASS,null, true);

        MContained lContained = isRoot ?
                                MContained.addRule(lContainedClass,MClass.ROOT_CLASS_GNAME).getFirst() :
                                MContained.get(lContainedClass, true);


        Severity.WARN.report(toString(),"","", (isRoot ? "ROOT " : "NON-ROOT ") + scope + " CONTAINED RULE ADDED: " + lContained);

        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE, lContained);
    }

    public boolean isRoot()
    {
        return isRoot;
    }

    public DefinitionScope getScope()
    {
        return scope;
    }

    private DefinitionScope scope;
    private boolean isRoot;
}

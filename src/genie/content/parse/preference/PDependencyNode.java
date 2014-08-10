package genie.content.parse.preference;

import genie.content.model.mclass.DefinitionScope;
import genie.content.model.mrelator.MRelator;
import genie.content.model.mrelator.RelatorType;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.model.ProcessorNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 8/1/14.
 */
public class PDependencyNode extends ParseNode
{
    public PDependencyNode(String aInName)
    {
        super(aInName);
        type = RelatorType.get(aInName);
    }

    protected void addParent(ProcessorNode aInParent)
    {
        super.addParent(aInParent);
        scope = DefinitionScope.get(aInParent.getName());
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        String lRelatingClass = DefinitionScope.CLASS == scope ?
                aInParentItem.getGID().getName() :
                aInData.getNamedValue(Strings.CLASS,null, true);

        MRelator lRel = MRelator.get(lRelatingClass,true);


//        Severity.WARN.report(toString(),"","", (isRoot ? "ROOT " : "NON-ROOT ") + scope + " CONTAINED RULE ADDED: " + lContained);

        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE, lRel);
    }

    public DefinitionScope getScope()
    {
        return scope;
    }

    public RelatorType getType() { return type; }

    private DefinitionScope scope;
    private RelatorType type;
}

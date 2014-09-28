package genie.content.parse.pdataowner;

import genie.content.model.mownership.DefinitionScope;
import genie.content.model.mownership.MOwner;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.model.ProcessorNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 9/27/14.
 */
public class POwnerNode extends ParseNode
{
    public POwnerNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        return new Pair<ParseDirective, Item>(
                ParseDirective.CONTINUE,
                MOwner.get(aInData.getNamedValue(Strings.NAME,"",true), true));
    }

    protected void addParent(ProcessorNode aInParent)
    {
        super.addParent(aInParent);
        scope = DefinitionScope.get(aInParent.getName());
    }

    private DefinitionScope scope = null;
}

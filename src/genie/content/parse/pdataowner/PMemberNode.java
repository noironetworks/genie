package genie.content.parse.pdataowner;

import genie.content.model.mownership.DefinitionScope;
import genie.engine.model.Item;
import genie.engine.parse.modlan.Node;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.model.ProcessorNode;
import genie.engine.parse.modlan.ParseDirective;

/**
 * Created by midvorki on 9/27/14.
 */
public class PMemberNode extends ParseNode
{
    public PMemberNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        return null;
    }

    protected void addParent(ProcessorNode aInParent)
    {
        super.addParent(aInParent);
        scope = DefinitionScope.get(aInParent.getName());
    }

    private DefinitionScope scope = null;
}
package genie.content.parse.pdataowner;

import genie.content.model.mownership.DefinitionScope;
import genie.content.model.mownership.MClassRule;
import genie.content.model.mownership.MModuleRule;
import genie.content.model.mownership.MOwner;
import genie.engine.model.Item;
import genie.engine.parse.modlan.Node;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.model.ProcessorNode;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 9/27/14.
 */
public class PClassNode extends ParseNode
{
    public PClassNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        MClassRule lRule = null;

        switch (scope)
        {
            case OWNER:

                lRule = new MClassRule((MOwner) aInParentItem, aInData.getNamedValue(Strings.NAME,Strings.ASTERISK, true));
                break;

            case MODULE:
            default:

                lRule = new MClassRule((MModuleRule) aInParentItem, aInData.getNamedValue(Strings.NAME,Strings.ASTERISK, true));
                break;
        }
        return new Pair<ParseDirective, Item>(
                        ParseDirective.CONTINUE,
                        lRule);
    }

    protected void addParent(ProcessorNode aInParent)
    {
        super.addParent(aInParent);
        scope = DefinitionScope.get(aInParent.getName());
    }

    private DefinitionScope scope = null;
}
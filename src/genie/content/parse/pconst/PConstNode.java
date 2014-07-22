package genie.content.parse.pconst;

import genie.content.model.mconst.*;
import genie.content.model.module.Module;
import genie.content.model.mtype.MType;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.model.ProcessorNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/21/14.
 */
public class PConstNode
        extends ParseNode
{
    /**
     * Constructor
     */
    public PConstNode(String aInName)
    {
        super(aInName);
        action = ConstAction.get(aInName);
    }

    protected void addParent(ProcessorNode aInParent)
    {
        super.addParent(aInParent);
        scope = ConstScope.get(aInParent.getName());
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        System.out.println(" ----------->" + this + ".beginCb(" + aInData + ", " + aInParentItem + ")");
        MConst lConst = new MConst(aInParentItem,aInData.getNamedValue(Strings.NAME,null,true),action,scope);
        String lIndirectionTarget = aInData.getNamedValue(Strings.TARGET,null,action.hasExplicitIndirection());
        if (!Strings.isEmpty(lIndirectionTarget))
        {
            new MIndirection(lConst,lIndirectionTarget);
        }
        String lValue = aInData.getNamedValue(Strings.VALUE,null,action.hasMandatoryValue());
        if (!Strings.isEmpty(lValue))
        {
            new MValue(lConst,lValue);
        }
        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE,lConst);
    }

    private ConstAction action;
    private ConstScope scope = null;
}

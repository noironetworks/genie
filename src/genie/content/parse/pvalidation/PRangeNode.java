package genie.content.parse.pvalidation;

import genie.content.model.mvalidation.*;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.model.ProcessorNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/22/14.
 */
public class PRangeNode
        extends ParseNode
{
    public PRangeNode(String aInName)
    {
        super(aInName);
        action = ValidatorAction.get(aInName);
    }

    protected void addParent(ProcessorNode aInParent)
    {
        super.addParent(aInParent);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        MRange lRange = new MRange(
                (MValidator)aInParentItem,
                aInData.getNamedValue(Strings.NAME,Strings.DEFAULT,true), action);

        addConstraints(aInData,lRange);
        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE,lRange);
    }

    private void addConstraints(Node aInData, MRange aInRange)
    {
        for (ConstraintValueType lConstrType : ConstraintValueType.values())
        {
            addConstraint(aInData,aInRange,lConstrType);
        }
    }

    private void addConstraint(Node aInData, MRange aInRange, ConstraintValueType aInType)
    {
        String lConstr = aInData.getNamedValue(aInType.getName(),null, false);
        if (!Strings.isEmpty(lConstr))
        {
            new MConstraintValue(aInRange, lConstr, aInType);
        }
    }
    private final ValidatorAction action;
}

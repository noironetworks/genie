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
public class PContentNode
        extends ParseNode
{
    public PContentNode(String aInName)
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
        MContentValidator lVal = new MContentValidator(
                (MValidator)aInParentItem,
                aInData.getNamedValue(Strings.NAME,Strings.DEFAULT,true), action);

        addConstraint(aInData, lVal);

        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE,lVal);
    }


    private void addConstraint(Node aInData, MContentValidator aInVal)
    {
        String lConstr = aInData.getNamedValue(Strings.REGEX,null, true);
        if (!Strings.isEmpty(lConstr))
        {
            new MContentValidatorParam(
                    aInVal,
                    aInData.getNamedValue(Strings.NAME,null,true),
                    lConstr,
                    aInData.getNamedValue(Strings.TYPE,null,false));
        }
    }

    private final ValidatorAction action;
}

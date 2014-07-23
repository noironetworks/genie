package genie.content.parse.pvalidation;

import genie.content.model.mvalidation.MRange;
import genie.content.model.mvalidation.MValidator;
import genie.content.model.mvalidation.ValidatorAction;
import genie.content.model.mvalidation.ValidatorScope;
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
        MRange lVal = new MRange(
                (MValidator)aInParentItem,
                aInData.getNamedValue(Strings.NAME,Strings.DEFAULT,true), action);

        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE,lVal);
    }

    private final ValidatorAction action;
}

package genie.content.parse.pvalidation;

import genie.content.model.mvalidation.MContentValidator;
import genie.content.model.mvalidation.MRange;
import genie.content.model.mvalidation.MValidator;
import genie.content.model.mvalidation.ValidatorAction;
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

        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE,lVal);
    }

    private final ValidatorAction action;
}

package genie.content.parse.pmodule;

import genie.content.model.module.Module;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;

/**
 * Created by midvorki on 7/21/14.
 */
public class PModuleNode
        extends ParseNode
{
    /**
     * Constructor
     */
    public PModuleNode(String aInName)
    {
        super(aInName, true);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        System.out.println("\n\n\n ----------->" + this + ".beginCb(" + aInData + ", " + aInParentItem + ") " + " \n\n\n");
        return new Pair<ParseDirective, Item>(
                    ParseDirective.CONTINUE,
                    Module.get(aInData.getNamedValue("name",null, true), true));
    }
}
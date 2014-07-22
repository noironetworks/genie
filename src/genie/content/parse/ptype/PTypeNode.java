package genie.content.parse.ptype;

import genie.content.model.module.Module;
import genie.content.model.mtype.MType;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/21/14.
 *
 * Parses type nodes
 *
 * module<name> <\br></\br>
 */
public class PTypeNode
        extends ParseNode
{
    public static final String PRIMITIVE = "primitive";

    /**
     * Constructor
     */
    public PTypeNode(String aInName)
    {
        super(aInName);
        isPrimitive = PRIMITIVE.equalsIgnoreCase(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        System.out.println("----------->" + this + ".beginCb(" + aInData + ", " + aInParentItem + ")");
        MType lType = new MType((Module) aInParentItem, aInData.getNamedValue(Strings.NAME,null,true), isPrimitive);
        lType.addSupertype(aInData.getNamedValue(Strings.SUPER, null, !isPrimitive));
        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE,lType);
    }

    public boolean isPrimitive()
    {
        return isPrimitive;
    }

    private final boolean isPrimitive;
}

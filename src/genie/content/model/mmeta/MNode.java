package genie.content.model.mmeta;

import genie.content.model.module.Module;
import genie.engine.model.*;

/**
 * Created by midvorki on 7/16/14.
 */
public class MNode extends Item
{
    public static final Cat MY_CAT = Cat.getCreate("parser:meta");
    public static final RelatorCat USES_REL = RelatorCat.getCreate("parse:meta:uses", Cardinality.SINGLE);

    public MNode(Item aInParent, String aInLName, Object aInContext)
    {
        super(MY_CAT, aInParent, aInLName);
        context = aInContext;
    }

    public void addUses(String aInTargetGName)
    {
        USES_REL.add(MY_CAT, getGID().getName(), MY_CAT, aInTargetGName);
    }

    public boolean usesOtherNode()
    {
        Relator lRel = USES_REL.getRelator(getGID().getName());
        return null != lRel && lRel.hasTo();
    }

    public Relator getUsesRelator()
    {
        return USES_REL.getRelator(getGID().getName());
    }

    public MNode getUsesNode()
    {
        Relator lRel = getUsesRelator();
        return (MNode) (null == lRel ? null : lRel.getToItem());
    }

    public Object getContext()
    {
        return context;
    }

    Object context;
}

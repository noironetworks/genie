package genie.content.model.mconst;

import genie.content.model.mprop.MProp;
import genie.content.model.mtype.MType;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import modlan.report.Severity;

/**
 * Created by midvorki on 7/9/14.
 */
public class MIndirection extends Item
{
    public static final Cat MY_CAT = Cat.getCreate("mconst:indirection");
    public static final String NAME = "indirection";

    public MIndirection(MConst aInConst, String aInConstName)
    {
        super(MY_CAT,aInConst,NAME);
        target = aInConstName;
        if (!aInConst.getAction().isRequireIndirectionTargetConst())
        {
            Severity.DEATH.report(
                    aInConst.toString(),
                    "definition of target const",
                    "can't specify target indirection",
                    "can't specify target const " + target + " for constant with action: " +
                    aInConst.getAction());
        }
    }

    public String getTargetName()
    {
        return target;
    }

    public MConst findTargetConst()
    {
        MConst lConst = null;
        Item lItem = getParent().getParent();
        if (lItem instanceof MType)
        {
            lConst = ((MType)lItem).findConst(getTargetName(), true);
        }
        else
        {
            lConst = ((MProp)lItem).findConst(getTargetName(), true);
        }
        if (null == lConst)
        {
            Severity.DEATH.report(
                    this.toString(),
                    "retrieval of target const",
                    "no constant found",
                    "can't find target const " + target + " in context of " +
                    lItem);
        }
        return lConst;
    }

    public MConst getConst()
    {
        return (MConst) getParent();
    }

    public MType getType()
    {
        return getConst().getType(false);
    }

    public MType getBaseType()
    {
        return getType().getBase();
    }

    private String target;

}

package genie.content.model.mconst;

import genie.content.model.mprop.MProp;
import genie.content.model.mtype.MType;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import modlan.report.Severity;

/**
 * Created by dvorkinista on 7/9/14.
 */
public class MConst extends Item
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CATEGORIES
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final Cat MY_CAT = Cat.getCreate("mconst");

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public MConst(
            MProp aInParent,
            String aInName,
            ConstAction aInAction)
    {
        this((Item) aInParent, aInName, aInAction);
    }

    public MConst(
            MType aInParent,
            String aInName,
            ConstAction aInAction)
    {
        this((Item)aInParent, aInName, aInAction);
    }

    private MConst(
            Item aInParent,
            String aInName,
            ConstAction aInAction)
    {
        super(MY_CAT, aInParent, aInName);
        action = (null == aInAction) ? ConstAction.VALUE : aInAction;
    }

    public ConstAction getAction()
    {
        return action;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // TYPE API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public MType getType(boolean aInIsBaseType)
    {
        Item lParent = getParent();
        MType lType =  lParent instanceof MType ? (MType) lParent : ((MProp) lParent).getType(false);
        if (null == lType)
        {
            Severity.DEATH.report(
                    this.toString(),
                    "const type retrieval",
                    "type definition not found",
                    "no type is resolvable for constant in context of " +
                    lParent);
        }
        return aInIsBaseType ? lType.getBase() : lType;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PROP API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MProp getProp(boolean aInBaseProp)
    {
        Item lParent = getParent();

        if (lParent instanceof MProp)
        {
            return aInBaseProp ? (((MProp)lParent).getBase()) : (MProp) lParent;
        }
        else
        {
            Severity.DEATH.report(
                    this.toString(),
                    "const property retrievale",
                    "no associated property found",
                    "const is contained by " +
                    lParent);

            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SUPER-CONST API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MConst getSuperConst()
    {
        if (ConstAction.REMOVE != getAction())
        {
            Item lParent = getParent();
            if (lParent instanceof MType)
            {
                MType lType = (MType) lParent;
                if (!lType.isBase())
                {
                    lType = lType.getSupertype();
                    if (null != lType)
                    {
                        lType.findConst(getLID().getName(), true);
                    }
                    else
                    {
                        Severity.DEATH.report(this.toString(), "retrieval of super const", "no supertype",
                                              "super type can't be found for non-base type " + lParent);
                    }
                }
            }
            else
            {
                MProp lProp = (MProp) lParent;
                if (!lProp.isBase())
                {
                    lProp = lProp.getOverridden(false);
                    if (null != lProp)
                    {
                        lProp.findConst(getLID().getName(), true);
                    }
                    else
                    {
                        Severity.DEATH.report(this.toString(), "retrieval of super const", "no super prop",
                                              "super Prop can't be found for non-base prop " + lParent);
                    }
                }
            }
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALUE API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MValue getValue()
    {
        return (MValue) getChildItem(MValue.MY_CAT,MValue.NAME);
    }

    public MValue findValue(boolean aInCheckSuper)
    {
        MValue lValue = null;
        for (MConst lConst = this;
             null != lConst && null == lValue;
             lConst = aInCheckSuper ? lConst.getSuperConst() : null)
        {
            lValue = lConst.getValue();
        }
        return lValue;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // INDIRECTION API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MIndirection getIndirection()
    {
        return (action.isRequireIndirectionTargetConst()) ?
                (MIndirection) getChildItem(MIndirection.MY_CAT,MIndirection.NAME) :
                null;

    }

    public MIndirection findIndirection(boolean aInCheckSuper)
    {
        MIndirection lIndir = null;
        if (action.isRequireIndirectionTargetConst())
        {
            for (MConst lConst = this;
                 null != lConst && null == lIndir;
                 lConst = aInCheckSuper ? lConst.getSuperConst() : null)
            {
                lIndir = lConst.getIndirection();
            }
            if (null == lIndir)
            {
                Severity.DEATH.report(
                        this.toString(),
                        "retrieval of const indirection",
                        "no const indirection found",
                        "const action " + getAction() +
                        " can't be satisfied");
            }
        }
        return lIndir;

    }

    public MConst findIndirectionConst(boolean aInCheckSuper)
    {
        MIndirection lInd = findIndirection(aInCheckSuper);
        return null == lInd ? null : lInd.findTargetConst();
    }



    private final ConstAction action;
}

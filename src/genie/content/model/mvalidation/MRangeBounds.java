package genie.content.model.mvalidation;

import genie.content.model.mconst.MConst;
import genie.engine.model.Cat;
import genie.engine.model.Item;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by midvorki on 7/10/14.
 */
public class MRangeBounds
        extends MConstraint
{
    public static final Cat MY_CAT = Cat.getCreate("mvalidator:mrange");

    public MRangeBounds(MValidator aInParent, String aInName, ValidatorAction aInActionOrNull)
    {
        super(MY_CAT, aInParent, aInName, aInActionOrNull);
    }

    public MRangeBounds getSuper()
    {
        return (MRangeBounds) getSuperConstraint();
    }

    public MConstraintValue getConstraintValue(ConstraintValueType aIn)
    {
        return (MConstraintValue) getChildItem(MConstraintValue.MY_CAT, aIn.getName());
    }

    public void getConstraintValue(Map<ConstraintValueType, MConstraintValue> aOut)
    {
        Collection<Item> lItems = new LinkedList<Item>();
        getChildItems(MConstraintValue.MY_CAT, lItems);
        for (Item lIt : lItems)
        {
            MConstraintValue lCV = (MConstraintValue) lIt;
            if (!aOut.containsKey(lCV.getConstraintValueType()))
            {
                aOut.put(lCV.getConstraintValueType(), lCV);
            }
        }
    }

    public MConstraintValue findConstraintValue(ConstraintValueType aIn, boolean aInIncludeSuper)
    {
        MConstraintValue lValue = null;
        for (MRangeBounds lRange = this;
             null != lRange && null == lValue;
             lRange = aInIncludeSuper ? lRange.getSuper() : null)
        {
            lValue = lRange.getConstraintValue(aIn);
        }
        return lValue;
    }

    public void findConstraintValue(Map<ConstraintValueType, MConstraintValue> aOut, boolean aInIncludeSuper)
    {
        MConstraintValue lValue = null;
        for (MRangeBounds lRange = this;
             null != lRange;
             lRange = aInIncludeSuper ? lRange.getSuper() : null)
        {
            getConstraintValue(aOut);
        }
    }
}

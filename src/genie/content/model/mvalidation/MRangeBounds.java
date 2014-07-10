package genie.content.model.mvalidation;

import genie.engine.model.Cat;

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
}

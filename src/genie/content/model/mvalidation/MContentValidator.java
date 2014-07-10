package genie.content.model.mvalidation;

import genie.engine.model.Cat;
import genie.engine.model.Item;

/**
 * Created by midvorki on 7/10/14.
 */
public class MContentValidator extends MConstraint
{
    public static final Cat MY_CAT = Cat.getCreate("mvalidator:mcontent");

    public MContentValidator(MValidator aInParent, String aInName, ValidatorAction aInActionOrNull)
    {
        super(MY_CAT, aInParent, aInName, aInActionOrNull);
    }

    public MContentValidator getSuper()
    {
        return (MContentValidator) getSuperConstraint();
    }
}

package genie.content.model.mvalidation;

import genie.content.model.mtype.MType;
import genie.engine.model.Cat;
import genie.engine.model.Item;

/**
 * Created by dvorkinista on 7/10/14.
 */
public class MConstraintValue
        extends Item
{
    public static final Cat MY_CAT = Cat.getCreate("mconst:value");
    public static final String NAME = "value";

    public MConstraintValue(MValidator aInConst, String aInValue, ConstraintValueType aInConstraintValueType)
    {
        super(MY_CAT,aInConst,aInConstraintValueType.getName());
        value = aInValue;
        constraintValueType = aInConstraintValueType;
    }

    public String getValue()
    {
        return value;
    }

    public MValidator getValidator()
    {
        return (MValidator) getParent();
    }

    public MType getType()
    {
        return getValidator().getType(false);
    }

    public MType getBaseType()
    {
        return getType().getBase();
    }

    public ConstraintValueType getConstraintValueType()
    {
        return constraintValueType;
    }

    private final String value;
    private final ConstraintValueType constraintValueType;
}
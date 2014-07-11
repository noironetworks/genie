package genie.content.model.mconst;

import modlan.report.Severity;

/**
 * Created by dvorkinista on 7/9/14.
 *
 * Constant behavior metadata directive. Constant action identifies instruction of how a constant clobbers previously
 * defined constant with the same name along the inheritance path. In addition, it specifies whether value of the
 * corresponding constant persists or transitions to another value.
 */
public enum ConstAction
{
    /**
     * specifies that this is an alias to another constant. think of it as synonymous or an alias.
     */
    MAPPED("mapped"), // MAPS into another constant for value

    /**
     * specifies that this constant defines a value and clobbers any previous definition (if value is specified.)
     */
    VALUE("value"),   // HAS value

    /**
     * indicates that the corresponding previously defined constant with the same name is not applicable.
     */
    REMOVE("remove"), // removes the constant

    /**
     * indicates that the constant has value, but the value of the constant never persists,
     * instead it goes back to the previous value
     */
    AUTO_REVERTIVE("auto-revertive"), // accepts the value but reverts to some original const

    /**
     * indicates that the constant has value, but the value of the constant never persists,
     * instead it goes to the value specified in the indirection rule.
     */
    AUTO_TRANSITION("auto-transition"), // automatically transition to specified const

    /**
     * specifies that the constant has value, and it serves as a default value for the properties for which is
     * constant is in scope.
     */
    DEFAULT("default"), // acts as default value

    /**
     * indicates that the constant has value, but can't be set administratively
     */
    UNSETTABLE("unsettable"), // unsettable administratively
    ;

    /**
     * Specifies if corresponding constant has explicitly identify indirection. That is whether this constant uses
     * other constant's value.
     */
    public boolean hasExplicitIndirection()
    {
        return DEFAULT == this || AUTO_TRANSITION == this  || MAPPED == this;
    }

    /**
     * Specifies if corresponding constant has implied indirection (as in reverts to previous value)
     */
    public boolean hasImplicitIndirection()
    {
        return AUTO_REVERTIVE == this;
    }

    /**
     * indicates if corresponding constant has a transient value that automatically transitions to another value.
     */
    public boolean isTransient()
    {
        return AUTO_REVERTIVE == this || AUTO_TRANSITION == this;
    }

    /**
     * identifies if corresponding constant has value.
     */
    public boolean hasValue()
    {
        switch (this)
        {
            case VALUE:
            case AUTO_REVERTIVE:
            case AUTO_TRANSITION:
            case UNSETTABLE:

                return true;

            default:

                return false;
        }
    }

    public boolean hasDirectOrIndirectValue()
    {
        return this != REMOVE;
    }
    /**
     * identifies if corresponding constant acts like an alias.
     */
    public boolean isMapped()
    {
        return DEFAULT == this || MAPPED == this;
    }

    /**
     * identifies if corresponding constant acts as a default indicator (points to a constant that provides default value)
     */
    public boolean isDefault()
    {
        return DEFAULT == this;
    }

    /**
     * constructor of const action
     * @param aIn name of the constant
     */
    private ConstAction(String aIn)
    {
        name = aIn;
    }

    /**
     * const action name accessor
     * @return name of the const action
     */
    public String getName()
    {
        return name;
    }

    /**
     * stringifier
     * @return name of the const action
     */
    public String toString()
    {
        return name;
    }

    /**
     * matches const action by name
     * @param aIn name of the const action to be matched
     * @return const action that matches the name
     */
    public static ConstAction get(String aIn)
    {
        for (ConstAction lCA : ConstAction.values())
        {
            if (aIn.equalsIgnoreCase(lCA.getName()))
            {
                return lCA;
            }
        }
        Severity.DEATH.report(
                "ConstAction",
                "get const action for name",
                "no such const action",
                "no support for " + aIn + "; actions supported: " + ConstAction.values());

        return null;
    }
    private final String name;
}

package genie.content.model.mconst;

/**
 * Created by dvorkinista on 7/11/14.
 *
 * Identifies whether the corresponding constant is bound to property ot a type
 */
public enum ConstScope
{
    /**
     * corresponding const is bound to a property
     */
    PROPERTY,
    /**
     * corresponding const is bound to a type
     */
    TYPE
    ;

    /**
     * stringifier
     */
    public String toString()
    {
        return super.toString().toLowerCase();
    }
}

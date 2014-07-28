package genie.content.model.mtype;

import genie.engine.model.Cat;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/28/14.
 */
public class MConstants
        extends SubTypeItem
{
    public static final Cat MY_CAT = Cat.getCreate("type:language:constants");
    public static final String NAME = "constants";
    public MConstants(
            MLanguageBinding aIn,
            DefinedIn aInDefinedIn,
            String aInPrefix,
            String aInSuffix)
    {
        super(MY_CAT, aIn, NAME);
        definedIn = null == aInDefinedIn ? DefinedIn.SPECIAL : aInDefinedIn;
        prefix = aInPrefix;
        suffix = aInSuffix;
    }

    /**
     * retrieves what format values are to be defined.
     * for example: dec, hex, octal, ...
     * @return format in which constant values are defined in
     */
    public DefinedIn getDefinedIn() { return definedIn; }
    /**
     * retrieves a literal prefix that is required before constant definition.
     * as in 0x.... or something like that
     * @return constant's literal prefix
     */
    public String getPrefix() { return prefix; }

    /**
     * @return whether the corresponding constants have literal prefix
     */
    public boolean hasPrefix() { return !Strings.isEmpty(prefix); }

    /**
     * retrieves literal suffix that is required before constant definition.
     * as in ll, ull: ... = ...6666ull
     * @return constant's literal suffix
     */
    public String getSuffix() { return suffix; }

    /**
     * @return whether the corresponding constants have literal suffix
     */
    public boolean hasSuffix() { return !Strings.isEmpty(suffix); }

    public MLanguageBinding getLanguageBinding()
    {
        return (MLanguageBinding) getParent();
    }

    /**
     * returns datatype that is used for these constants
     * @return datatype used for these constants
     */
    public MType getType()
    {
        MConstraints lConstrs = getLanguageBinding().getConstraints();
        if (null != lConstrs)
        {
            return getType();
        }
        else
        {
            return super.getMType();
        }
    }

    /**
     * specifies what format values are to be defined.
     * for example: dec, hex, octal, ...
     */
    private final DefinedIn definedIn;
    /**
     * a literal prefix that is required before constant definition.
     * as in 0x.... or something like that
     */
    private final String prefix;
    /**
     * literal suffix that is required before constant definition.
     * as in ll, ull: ... = ...6666ull
     */
    private final String suffix;
}

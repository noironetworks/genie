package modlan.utils;

/**
 * Created by midvorki on 7/10/14.
 */
public class Strings
{
    public static final String ANY = "any";
    public static final String DEFAULT = "default";
    public static final String EMPTY = "";
    public static final String MAX = "max";
    public static final String METADATA = "metadata";
    public static final String META = "meta";
    public static final String MIN = "min";
    public static final String NAME = "name";
    public static final String NO = "no";
    public static final String PROP = "prop";
    public static final String QUAL = "qual";
    public static final String REGEX = "regex";
    public static final String SUPER = "super";
    public static final String TARGET = "target";
    public static final String TYPE = "type";
    public static final String TYPEDEF = "typedef";
    public static final String VALUE = "value";
    public static final String YES = "yes";

    public static boolean isEmpty(String aIn) { return null == aIn || aIn.isEmpty(); }

    public static boolean isAny(String aIn) { return isEmpty(aIn) || ANY.equalsIgnoreCase(aIn); }

    public static boolean isDefault(String aIn) { return DEFAULT.equalsIgnoreCase(aIn); }

    public static String replaceChar(String aIn, int aInPos, char aInNewChar)
    {
        if (null != aIn && aInPos < aIn.length())
        {
            char[] lChars = aIn.toCharArray();
            lChars[aInPos] = aInNewChar;
            return new String(lChars);
        }
        else
        {
            return aIn;
        }
    }

    public static String upFirstLetter(String aIn)
    {
        if (!isEmpty(aIn))
        {
            char lChar = aIn.charAt(0);
            if (Character.isLetter(lChar))
            {
                if (Character.isLowerCase(lChar))
                {
                    return replaceChar(aIn,0,Character.toUpperCase(lChar));
                }
            }
        }
        return aIn;
    }

}

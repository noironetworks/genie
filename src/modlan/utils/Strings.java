package modlan.utils;

/**
 * Created by midvorki on 7/10/14.
 */
public class Strings
{
    public static final String DEFAULT = "default";
    public static final String ANY = "any";

    public static boolean isEmpty(String aIn) { return null == aIn || aIn.isEmpty(); }

    public static boolean isAny(String aIn) { return isEmpty(aIn) || ANY.equalsIgnoreCase(aIn); }

    public static boolean isDefault(String aIn) { return DEFAULT.equalsIgnoreCase(aIn); }

}

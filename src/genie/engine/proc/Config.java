package genie.engine.proc;

/**
 * Created by midvorki on 10/8/14.
 */
public class Config
{
    public static String CONFIG_FILE_NAME = "genie.cfg";

    public static String getWorkingPath() { return workingPath; }
    public static String getConfigPath() { return configPath; }
    public static String getSyntaxPath() { return syntaxPath; }
    public static String getSyntaxSuffix() { return syntaxSuffix; }
    public static String getLoaderPath() { return loaderPath; }
    public static String getLoaderSuffix() { return loaderSuffix; }
    public static String getGenDestPath() { return genDestPath; }
    public static String getLogDirParent() { return logDirParent; }

    public static void setSyntaxRelPath(String aIn, String aInSuffix)
    {
        syntaxPath = concatPath(workingPath,aIn);
        syntaxSuffix = aInSuffix;
    };

    public static void setLoaderRelPath(String aIn, String aInSuffix)
    {
        loaderPath = concatPath(workingPath,aIn);
        loaderSuffix = aInSuffix;
    }

    public static void setLogDirParent(String aIn)
    {
        logDirParent = aIn;
    }

    public static void setGenDestPath(String aIn)
    {
        genDestPath = aIn;
    }

    private static String initWorkingPath()
    {
        return System.getProperty("user.dir");
    }

    private static String initConfigFile()
    {
        return concatPath(System.getProperty("user.dir"), CONFIG_FILE_NAME);
    }

    private static String concatPath(String aInP1, String aInP2)
    {
        return aInP1 + ((aInP1.endsWith("/") || aInP2.startsWith("/")) ? "" : "/") + aInP2;
    }

    public static String print()
    {
        return "genie:config(config path: " + configPath + "; syntax path: " + syntaxPath + "; loader path: " + loaderPath + ")";
    }

    public static String workingPath = initWorkingPath();
    public static String syntaxPath = null;
    public static String syntaxSuffix = null;
    public static String loaderPath = null;
    public static String loaderSuffix = null;
    public static String genDestPath = null;
    public static String configPath = initConfigFile();
    public static String logDirParent = null;
}

package genie.engine.format;

/**
 * Created by midvorki on 7/24/14.
 */
public class FormatterCtx
{
    public FormatterCtx(String aInDestDir)
    {
        rootPath = aInDestDir;
    }

    public String getRootPath()
    {
        return rootPath;
    }

    public String toString()
    {
        return "formatter-ctx(" + getRootPath() + ')';
    }

    private final String rootPath;
}

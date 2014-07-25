package genie.engine.format;

import genie.engine.file.WriteStats;

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

    public WriteStats getStats() { return stats; }

    public String toString()
    {
        return "formatter-ctx(" + getRootPath() + ')';
    }

    private final String rootPath;
    private final WriteStats stats = new WriteStats();
}

package genie.engine.format;

/**
 * Created by midvorki on 7/24/14.
 *
 * Specifies name formation rule for generated file
 * <*projectpath*>/ralativepath/modulepath/[fileprefix][*FILENAME*][filesuffix][.filextension]
 */
public class FileNameRule
{
    /**
     * Constructor
     * <*projectpath*>/ralativepath/modulepath/[fileprefix][*FILENAME*][filesuffix][.filextension]
     */
    public FileNameRule(
            final String aInRelativePath,
            final String aInModulePath,
            final String aInFilePrefix,
            final String aInFileSuffix,
            final String aInFileExtension)
    {
        relativePath = aInRelativePath;
        modulePath = aInModulePath;
        filePrefix = aInFilePrefix;
        fileSuffix = aInFileSuffix;
        fileExtension = aInFileExtension;
    }

    public final String getRelativePath()
    {
        return relativePath;
    }
    public final String getModulePath()
    {
        return modulePath;
    }
    public final String getFilePrefix()
    {
        return filePrefix;
    }

    public final String getFileSuffix()
    {
        return fileSuffix;
    }

    public final String getFileExtension()
    {
        return fileExtension;
    }

    private final String relativePath;
    private final String modulePath;
    private final String filePrefix;
    private final String fileSuffix;
    private final String fileExtension;

}

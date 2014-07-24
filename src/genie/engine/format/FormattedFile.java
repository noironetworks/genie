package genie.engine.format;

import genie.engine.file.WriteStats;
import genie.engine.file.Writer;
import modlan.report.Severity;
import modlan.utils.Strings;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by midvorki on 7/23/14.
 */
public class FormattedFile
{
    public FormattedFile(
            String aInRootPath,
            String aInRelativePath,
            String aInFilePrefix,
            String aInFileName,
            String aInFileSuffix,
            String aInFileExtension,
            boolean aInOverrideExisting,
            WriteStats aInStats)
    {
        rootPath = aInRootPath;
        relativePath = aInRelativePath;
        filePrefix = aInFilePrefix;
        fileName = aInFileName;
        fileSuffix = aInFileSuffix;
        fileExtension = aInFileExtension;
        overrideExisting = aInOverrideExisting;
        stats = aInStats;
        init();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PRINTING API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void print(boolean aInValue)
    {
        writer.print(aInValue);
    }

    public void print(char aInValue)
    {
        writer.print(aInValue);
    }

    public void print(char[] aInValue)
    {
        writer.print(aInValue);
    }

    public void print(double aInValue)
    {
        writer.print(aInValue);
    }

    public void print(float aInValue)
    {
        writer.print(aInValue);
    }

    public void print(int aInValue)
    {
        writer.print(aInValue);
    }

    public void print(long aInValue)
    {
        writer.print(aInValue);
    }

    public void print(Object aInValue)
    {
        writer.print(aInValue);
    }

    public void print(String aInValue)
    {
        writer.print(aInValue);
    }

    public void print(String format, Object... args)
    {
        writer.format(format, args);
    }

    public void println()
    {
        writer.println();
    }

    public void println(boolean aInValue)
    {
        writer.println(aInValue);
    }

    public void println(char aInValue)
    {
        writer.println(aInValue);
    }

    public void println(char[] aInValue)
    {
        writer.println(aInValue);
    }

    public void println(double aInValue)
    {
        writer.println(aInValue);
    }

    public void println(float aInValue)
    {
        writer.println(aInValue);
    }

    public void println(int aInValue)
    {
        writer.println(aInValue);
    }

    public void println(long aInValue)
    {
        writer.println(aInValue);
    }

    public void println(Object aInValue)
    {
        writer.println(aInValue);
    }

    public void println(String aInValue)
    {
        writer.println(aInValue);
    }

    public void println(String format, Object... args)
    {
        writer.format(format, args);
        writer.println();
    }


    public void flush()
    {
        writer.flush();
    }

    public void close()
    {
        writer.close();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DATA ACCESSORS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String getFullPath()
    {
        return fullPath;
    }

    public String getDirPath()
    {
        return dirPath;
    }

    public String getFullFileName()
    {
        return fullFileName;
    }

    public String getRootPath()
    {
        return rootPath;
    }

    public String getRelativePath()
    {
        return relativePath;
    }

    public String getFilePrefix()
    {
        return fileSuffix;
    }

    public String getFileName()
    {
        return fileName;
    }

    public String getFileSuffix()
    {
        return fileSuffix;
    }

    public String getFileExtension()
    {
        return fileExtension;
    }

    public boolean isOverrideExisting()
    {
        return overrideExisting;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // INITIALIZATION
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initFullPath()
    {
        StringBuilder lSb = new StringBuilder();
        if (!Strings.isEmpty(rootPath))
        {
            lSb.append(rootPath);
            if (!rootPath.endsWith("/"))
            {
                lSb.append('/');
            }
        }
        if (!Strings.isEmpty(relativePath))
        {
            lSb.append(relativePath);
            if (!relativePath.endsWith("/"))
            {
                lSb.append('/');
            }
        }
        dirPath = lSb.toString();

        StringBuilder lFnSb = new StringBuilder();

        if (!Strings.isEmpty(filePrefix))
        {
            lFnSb.append(filePrefix);

        }
        if (!Strings.isEmpty(fileName))
        {
            lFnSb.append(fileName);
        }
        if (!Strings.isEmpty(fileSuffix))
        {
            lFnSb.append(fileSuffix);
        }
        if (!Strings.isEmpty(fileExtension))
        {
            lFnSb.append('.');
            lFnSb.append(fileExtension);
        }
        lSb.append(lFnSb);
        fullPath = lSb.toString();
        fullFileName = lFnSb.toString();
    }

    public File getDir()
    {
        return dir;
    }

    public File getFile()
    {
        return file;
    }

    private void init()
    {
        initFullPath();
        initDir();
        initFile();
        initWriter();
    }

    private synchronized void initDir()
    {
        try
        {
            dir = new File(dirPath).getCanonicalFile();
        }
        catch (Throwable lE)
        {
            Severity.DEATH.report(toString(),"initialize directory","","", lE);
        }
        for  (int i = 0; i < 10 && !dir.exists(); i++)
        {
            if (!dir.mkdirs())
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (Throwable lE)
                {

                }
            }
        }
        if (!dir.exists())
        {
            Severity.DEATH.report(toString(),"initialize directory","", "unable to init directory: " + dirPath);
        }
    }


    private synchronized void initFile()
    {
        File file = new File(fullPath); //new File(dir, aInFileName);
        if ((!overrideExisting) && file.exists())
        {
            file = new File(fullPath + ".new");// new File(dir, aInFileName + ".new");
        }
    }

    private synchronized void initWriter()
    {
        try
        {
            String lFileName = file.toString();
            writer = new PrintWriter(new Writer(file, stats));
            //return new PrintWriter(
            //    new BufferedWriter(
            //        new Writer(file),
            //        1024 * 1024 * 1)); // 1 mB buffer
        }
        catch (Throwable lE)
        {
            Severity.DEATH.report(toString(),"initialize writer","","", lE);

        }
    }

    public String toString()
    {
        return "formatted-file(" + fullPath + ")";
    }

    private String dirPath = null;
    private String fullPath = null;
    private String fullFileName = null;

    private final String rootPath;
    private final String relativePath;
    private final String fileName;
    private final String filePrefix;
    private final String fileSuffix;
    private final String fileExtension;
    private final boolean overrideExisting;
    private File dir = null;
    private File file = null;
    private PrintWriter writer = null;
    private final WriteStats stats;
}

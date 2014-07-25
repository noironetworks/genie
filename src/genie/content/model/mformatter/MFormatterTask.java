package genie.content.model.mformatter;

import genie.engine.format.FileTypeMeta;
import genie.engine.format.FormatterRegistry;
import genie.engine.format.FormatterTaskType;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import modlan.report.Severity;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/24/14.
 */
public class MFormatterTask
        extends Item
{
    /**
     * category of this item
     */
    public static final Cat MY_CAT = Cat.getCreate("formatter:task");

    /**
     * Constructor
     * @param aInLName name of the feature
     */
    public MFormatterTask(MFormatterFeature aInDomain, String aInLName)
    {
        super(MY_CAT, aInDomain, aInLName);
    }

    public MFormatterFeature getFeature()
    {
        return (MFormatterFeature) getParent();
    }

    public MFormatterDomain getDomain()
    {
        return getFeature().getDomain();
    }

    public FormatterTaskType getTarget() { return target; }
    public void setTarget(FormatterTaskType aIn) { target = aIn; }
    public void setTarget(String aIn) { setTarget(FormatterTaskType.get(aIn)); }

    public Cat getTargetCategory() { return targetCatOrNull; }
    public void setTargetCategory(Cat aIn) { targetCatOrNull = aIn; }
    public void setTargetCategory(String aIn)
    {
        if (!Strings.isEmpty(aIn))
        {
            Cat lCat = Cat.get(aIn);
            if (null != lCat)
            {
                setTargetCategory(lCat);
            }
            else
            {
                Severity.WARN.report(
                        toString(),"set target category","category not found", "category doesn't exist: " + aIn);
            }
        }
    }

    public String getRelativePath() { return relativePath; }
    public void setRelativePath(String aIn) { relativePath = aIn; }

    public FileTypeMeta getFileType() { return fileType; }
    public void setFileType(FileTypeMeta aIn) { fileType = aIn; }
    public void setFileType(String aIn) { setFileType(FileTypeMeta.get(aIn));}

    public String getFilePrefix() { return filePrefix; }
    public void setFilePrefix(String aIn) { filePrefix = aIn; }

    public String getFileSuffix() { return fileSuffix; }
    public void setFileSuffix(String aIn) { fileSuffix = aIn; }

    public Class getFormatterClass() { return formatterClass; }
    public void setFormatterClass(String aIn)
    {
        try
        {
            formatterClass = ClassLoader.getSystemClassLoader().loadClass(aIn);
        }
        catch (Throwable lE)
        {
            Severity.DEATH.report(toString(),"formatter class specification", "class not found: " + aIn, lE);
        }
    }

    private FormatterTaskType target = null;
    private Cat targetCatOrNull = null;
    private String relativePath = null;
    private FileTypeMeta fileType = null;
    private String filePrefix = null;
    private String fileSuffix = null;
    private Class formatterClass = null;
}
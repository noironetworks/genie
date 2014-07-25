package genie.engine.format;

/**
 * Created by midvorki on 7/24/14.
 */
public class FormatterTaskMeta
{
    public FormatterTaskMeta(
            String aInName,
            FormatterTaskType aInType,
            FileTypeMeta aInFile,
            FileNameRule aInFileNameRule,
            Class aInTaskClass)
    {
        name = aInName;
        type = aInType;
        file = aInFile;
        taskClass = aInTaskClass;
        fileNameRule = aInFileNameRule;
    }

    public String getName() { return name; }
    public FormatterTaskType getType() { return type; }
    public FileTypeMeta getFile() { return file; }

    public boolean isEnabled()
    {
        return isEnabled;
    }

    public void setEnabled(boolean aIn)
    {
        isEnabled = aIn;
    }

    public void process(FormatterCtx aInCtx)
    {
        // TODO:IMPLEMENT ME
        switch (type)
        {
            case GENERIC:

                break;

            case CATEGORY:

                break;

            case ITEM:

                break;

            default:

                // DO NOTHING. NOT POSSIBLE
        }
    }

    public String toString()
    {
        return "formatter:task(" + name + ')';
    }

    private final String name;
    private final FormatterTaskType type;
    private final FileTypeMeta file;
    private final FileNameRule fileNameRule;
    private final Class taskClass;
    private boolean isEnabled = true;
}

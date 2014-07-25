package genie.engine.format;

import genie.engine.file.WriteStats;
import genie.engine.proc.Processor;
import modlan.report.Severity;

import java.lang.reflect.Constructor;

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
            default: // TODO: REMOVE
                try
                {
                    Constructor<FormatterTask> lConstr =
                            taskClass.getConstructor(
                                    FormatterCtx.class,
                                    FileNameRule.class,
                                    Indenter.class,
                                    BlockFormatDirective.class,
                                    BlockFormatDirective.class,
                                    String.class,
                                    boolean.class,
                                    WriteStats.class);
                    if (null != lConstr)
                    {
                        FormatterTask lTask = lConstr.newInstance(
                                aInCtx,
                                fileNameRule,
                                file.getIndenter(),
                                file.getHeaderFormatDirective(),
                                file.getCommentFormatDirective(),
                                name,
                                false, // isUser, TODO: FIX
                                aInCtx.getStats()
                                );

                        Processor.get().getDsp().trigger(lTask);
                    }
                    else
                    {
                        Severity.DEATH.report(
                                toString(),
                                "process",
                                "failed to constract",
                                "constructor not found for class:" + taskClass);
                    }
                }
                catch (Throwable lE)
                {
                    Severity.DEATH.report(toString(),"process","failed to constract", lE);
                }

                break;

            /**
            case CATEGORY:

                break;

            case ITEM:

                break;

            default:

                // DO NOTHING. NOT POSSIBLE
             **/
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

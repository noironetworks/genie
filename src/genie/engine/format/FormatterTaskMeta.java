package genie.engine.format;

import genie.engine.file.WriteStats;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import genie.engine.model.Node;
import genie.engine.proc.Processor;
import modlan.report.Severity;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.LinkedList;

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
            Class aInTaskClass,
            Cat aInCatOrNull)
    {
        name = aInName;
        type = aInType;
        file = aInFile;
        fileNameRule = aInFileNameRule;
        taskClass = aInTaskClass;
        catOrNull = aInCatOrNull;
        try
        {
            switch (aInType)
            {
                case ITEM:
                {
                    taskConstr = taskClass.getConstructor(
                            FormatterCtx.class,
                            FileNameRule.class,
                            Indenter.class,
                            BlockFormatDirective.class,
                            BlockFormatDirective.class,
                            String.class,
                            boolean.class,
                            WriteStats.class,
                            Cat.class,
                            Item.class);
                    break;
                }
                case CATEGORY:
                {
                    taskConstr = taskClass.getConstructor(
                                        FormatterCtx.class,
                                        FileNameRule.class,
                                        Indenter.class,
                                        BlockFormatDirective.class,
                                        BlockFormatDirective.class,
                                        String.class,
                                        boolean.class,
                                        WriteStats.class,
                                        Cat.class);
                    break;
                }
                case GENERIC:
                default:
                {
                    taskConstr = taskClass.getConstructor(
                                        FormatterCtx.class,
                                        FileNameRule.class,
                                        Indenter.class,
                                        BlockFormatDirective.class,
                                        BlockFormatDirective.class,
                                        String.class, boolean.class,
                                        WriteStats.class);
                    break;
                }
            }
        }
        catch (Throwable lE)
        {
            Severity.DEATH.report(toString(),"process","failed to constract", lE);
        }
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
        try
        {
            switch (type)
            {
                case ITEM:
                {
                    for (Item lItem : catOrNull.getNodes().getItemsList())
                    {
                        FormatterTask lTask = taskConstr.newInstance(
                                aInCtx,
                                fileNameRule,
                                file.getIndenter(),
                                file.getHeaderFormatDirective(),
                                file.getCommentFormatDirective(),
                                name,
                                false, // isUser, TODO: FIX
                                aInCtx.getStats(),
                                catOrNull,
                                lItem);
                        Processor.get().getDsp().trigger(lTask);

                    }
                    break;
                }

                case CATEGORY:
                {
                    FormatterTask lTask = taskConstr.newInstance(
                            aInCtx,
                            fileNameRule,
                            file.getIndenter(),
                            file.getHeaderFormatDirective(),
                            file.getCommentFormatDirective(),
                            name,
                            false, // isUser, TODO: FIX
                            aInCtx.getStats(),
                            catOrNull
                            );

                    Processor.get().getDsp().trigger(lTask);

                    break;
                }
                case GENERIC:
                default:
                {
                    FormatterTask lTask = taskConstr.newInstance(
                                aInCtx,
                                fileNameRule,
                                file.getIndenter(),
                                file.getHeaderFormatDirective(),
                                file.getCommentFormatDirective(),
                                name,
                                false, // isUser, TODO: FIX
                                aInCtx.getStats());

                    Processor.get().getDsp().trigger(lTask);

                    break;
                }
            }
        }
        catch (Throwable lE)
        {
            Severity.DEATH.report(toString(), "process", "failed to constract", lE);
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
    private final Cat catOrNull;
    private final Class taskClass;
    Constructor<FormatterTask> taskConstr;
    private boolean isEnabled = true;
}

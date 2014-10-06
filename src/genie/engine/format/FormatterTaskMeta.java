package genie.engine.format;

import genie.engine.file.WriteStats;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import genie.engine.proc.Processor;
import modlan.report.Severity;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

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
            Class<FormatterTask> aInTaskClass,
            Cat aInCatOrNull,
            boolean aInIsUser)
    {
        name = aInName;
        type = aInType;
        file = aInFile;
        fileNameRule = aInFileNameRule;
        taskClass = aInTaskClass;
        catOrNull = aInCatOrNull;
        taskConstr = initConstructor(aInType);
        acceptor = initAcceptor(aInType);
        isUser = aInIsUser;
    }

    private Method initAcceptor(FormatterTaskType aInType)
    {
        Method lMethod = null;
        try
        {
            switch (aInType)
            {
                case ITEM:
                {
                    lMethod = taskClass.getMethod("shouldTriggerTask", Item.class);
                }
                default:
                {
                    lMethod = taskClass.getMethod("shouldTriggerTask");
                }
            }
        }
        catch (Throwable lT)
        {

        }
        if (null == lMethod)
        {
            Severity.INFO.report(toString(), "acceptor method init", "no acceptor method defined", "");
        }
        return lMethod;
    }

    private Constructor<FormatterTask> initConstructor(FormatterTaskType aInType)
    {
        Constructor<FormatterTask> lConst = null;
        try
        {
            switch (aInType)
            {
                case ITEM:
                {
                    lConst = taskClass.getConstructor(
                            FormatterCtx.class,
                            FileNameRule.class,
                            Indenter.class,
                            BlockFormatDirective.class,
                            BlockFormatDirective.class,
                            String.class,
                            boolean.class,
                            WriteStats.class,
                            // NO NEED FOR CATEGRY: Cat.class,
                            Item.class);
                    break;
                }
                case CATEGORY:
                {
                    lConst = taskClass.getConstructor(
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
                    lConst = taskClass.getConstructor(
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
        return lConst;
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

    private static String getModuleContext(Item aInIt)
    {
        Item lParentIt = aInIt.getParent();
        return null == lParentIt ? null : lParentIt.getGID().getName();
    }

    private boolean doAccept(Object aIn)
    {
        boolean lRet = true;
        try
        {
            lRet = ((null == acceptor) ||
                    ((null == aIn) ?
                        ((Boolean) acceptor.invoke(null)).booleanValue() :
                        ((Boolean) acceptor.invoke(null, aIn)).booleanValue()));
        }
        catch (Throwable lT)
        {
            Severity.DEATH.report(toString(),"check acceptance", "unexpected exception encountered; check the acceptor/shouldTriggerTask method on your formatter task: " + taskClass, lT);
        }
        return lRet;
    }

    public void process(FormatterCtx aInCtx)
    {
        //System.out.println(this + ".process()");
        try
        {
            switch (type)
            {
                case ITEM:
                {
                    for (Item lItem : catOrNull.getNodes().getItemsList())
                    {
                        if (doAccept(lItem))
                        {
                            FormatterTask lTask = taskConstr.newInstance(
                                                    aInCtx,
                                                    fileNameRule.makeSpecific(getModuleContext(lItem)),
                                                    file.getIndenter(),
                                                    file.getHeaderFormatDirective(),
                                                    file.getCommentFormatDirective(),
                                                    lItem.getLID().getName(), //name,
                                                    isUser,
                                                    aInCtx.getStats(),
                                                    //NO NEED FOR CATEGORY: catOrNull,
                                                    lItem);
                            Processor.get().getDsp().trigger(lTask);
                        }
                        else
                        {
                            Severity.INFO.report(toString(),"process", "task not accepted for: " + lItem, "this is normal, check the acceptor/shouldTriggerTask method on your formatter task:" + taskClass);
                        }
                    }
                    break;
                }

                case CATEGORY:
                {
                    if (doAccept(null))
                    {
                        FormatterTask lTask = taskConstr.newInstance(
                                aInCtx,
                                fileNameRule,
                                file.getIndenter(),
                                file.getHeaderFormatDirective(),
                                file.getCommentFormatDirective(),
                                name,
                                isUser,
                                aInCtx.getStats(),
                                catOrNull);

                        Processor.get().getDsp().trigger(lTask);
                    }
                    else
                    {
                        Severity.INFO.report(toString(),"process", "task not accepted for: " + catOrNull, "this is normal, check the acceptor/shouldTriggerTask method on your formatter task:" + taskClass);
                    }

                    break;
                }
                case GENERIC:
                default:
                {
                    if (doAccept(null))
                    {
                        FormatterTask lTask = taskConstr.newInstance(
                                aInCtx,
                                fileNameRule,
                                file.getIndenter(),
                                file.getHeaderFormatDirective(),
                                file.getCommentFormatDirective(),
                                name,
                                isUser,
                                aInCtx.getStats());

                        Processor.get().getDsp().trigger(lTask);
                    }
                    else
                    {
                        Severity.INFO.report(toString(),"process", "task not accepted", "this is normal, check the acceptor/shouldTriggerTask method on your formatter task:" + taskClass);
                    }
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
    private final Class<FormatterTask> taskClass;
    private final Constructor<FormatterTask> taskConstr;
    private final Method acceptor;
    private final boolean isUser;
    private boolean isEnabled = true;
}

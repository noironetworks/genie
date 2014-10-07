package genie.content.format.build.automake.cpp;

import genie.content.model.mclass.MClass;
import genie.content.model.module.Module;
import genie.engine.file.WriteStats;
import genie.engine.format.*;
import genie.engine.model.Item;

/**
 * Created by midvorki on 10/7/14.
 */
public class FLibPcIn
        extends ItemFormatterTask
{
    public static final String FORMAT = "prefix=@prefix@\n" + "exec_prefix=@exec_prefix@\n" + "libdir=@libdir@\n"
                                        + "includedir=@includedir@\n" + "\n" + "Name: @PACKAGE@\n"
                                        + "Description: OpFlex Framework Generated Model\n" + "Version: @VERSION@\n"
                                        + "Libs: -L${libdir} -l@PACKAGE@\n" + "Libs.private: @LIBS@\n"
                                        + "Cflags: -I${includedir} @BOOST_CPPFLAGS@\n";

    public FLibPcIn(
            FormatterCtx aInFormatterCtx, FileNameRule aInFileNameRule, Indenter aInIndenter, BlockFormatDirective aInHeaderFormatDirective, BlockFormatDirective aInCommentFormatDirective, boolean aInIsUserFile, WriteStats aInStats, Item aInItem
                     )
    {
        super(aInFormatterCtx,
              aInFileNameRule,
              aInIndenter,
              aInHeaderFormatDirective,
              aInCommentFormatDirective,
              aInIsUserFile,
              aInStats,
              aInItem);
    }

    /**
     * Optional API required by framework to regulate triggering of tasks.
     * This method identifies whether this task should be triggered for the item passed in.
     * @param aIn item for which task is considered to be triggered
     * @return true if task shouold be triggered, false if task should be skipped for this item.
     */
    public static boolean shouldTriggerTask(Item aIn)
    {
        return MClass.hasConcreteClassDefs((Module) aIn);
    }

    /**
     * Optional API required by the framework for transformation of file naming rule for the corresponding
     * generated file. This method can customize the location for the generated file.
     * @param aInFnr file name rule template
     * @param aInItem item for which file is generated
     * @return transformed file name rule
     */
    public static FileNameRule transformFileNameRule(FileNameRule aInFnr,Item aInItem)
    {
        String lTargetModue = ((Module)aInItem).getLID().getName().toLowerCase();
        String lLibName = "lib" + lTargetModue + "model";
        FileNameRule lFnr = new FileNameRule(
                aInFnr.getRelativePath() +  lTargetModue + "model",
                null,
                aInFnr.getFilePrefix(),
                aInFnr.getFileSuffix(),
                aInFnr.getFileExtension(),
                lLibName);

        return lFnr;
    }

    public void firstLineCb()
    {
        out.println("#!/bin/sh");
    }


    public void generate()
    {
        //Module lModule = (Module) getItem();
        //String lModuleName = lModule.getLID().getName().toLowerCase();
        //String lLibName = "lib" + lModuleName + "model";
        out.println(FORMAT);//.replaceAll("_MODULE_NAME_", lModuleName).replaceAll("_LIB_NAME_", lLibName));
    }
}

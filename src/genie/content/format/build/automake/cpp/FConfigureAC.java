package genie.content.format.build.automake.cpp;

import genie.content.model.mclass.MClass;
import genie.content.model.module.Module;
import genie.engine.file.WriteStats;
import genie.engine.format.*;
import genie.engine.model.Item;

/**
 * Created by midvorki on 10/7/14.
 */
public class FConfigureAC
        extends ItemFormatterTask
{
    public static final String FORMAT = "#\n" + "# Process this file with autoconf to produce a configure script\n"
                                        + "#\n" + "# If you just want to start a build from source control, run\n"
                                        + "# autogen.sh first.\n" + "#\n" + "\n"
                                        + "# ---------------------------------------------------------------\n"
                                        + "# Initialization\n" + "\n" + "AC_INIT([_MODULE_NAME_], [1.0.0])\n"
                                        + "AC_SUBST(MODEL_NAME, [\"_MODULE_NAME_ Generated OpFlex Model\"])\n" + "\n"
                                        + "# initialize automake and libtool\n" + "AM_INIT_AUTOMAKE([subdir-objects])\n"
                                        + "AM_CONFIG_HEADER(config.h)\n" + "LT_INIT()\n" + "AC_PROG_LIBTOOL\n"
                                        + "AC_CONFIG_MACRO_DIR([m4])\n" + "\n"
                                        + "# ---------------------------------------------------------------\n"
                                        + "# Environment introspection\n" + "\n" + "# check for compiler\n"
                                        + "AC_PROG_CC\n" + "AC_PROG_CXX\n" + "AC_PROG_INSTALL\n" + "AM_PROG_AS\n"
                                        + "AC_LANG([C++])\n" + "\n" + "# check for doxygen\n"
                                        + "AC_CHECK_PROGS(DOXYGEN,doxygen,none)\n"
                                        + "AM_CONDITIONAL(HAVE_DOXYGEN, [test x$DOXYGEN != 'xnone']) \n" + "\n"
                                        + "# ---------------------------------------------------------------\n"
                                        + "# Dependency checks\n" + "\n" + "# Checks for header files\n"
                                        + "AC_STDC_HEADERS\n" + "\n"
                                        + "AX_BOOST_BASE([1.52.0], [], AC_MSG_ERROR([Boost is required]))\n"
                                        + "PKG_CHECK_MODULES([libopflex], [libopflex >= 0.1.0])\n" + "\n"
                                        + "# Older versions of autoconf don't define docdir\n"
                                        + "if test x$docdir = x; then\n"
                                        + "\tAC_SUBST(docdir, ['${prefix}/share/doc/'$PACKAGE])\n" + "fi\n" + "\n"
                                        + "# ---------------------------------------------------------------\n"
                                        + "# Output\n" + "\n" + "AC_CONFIG_FILES([\\\n" + "\tMakefile \\\n"
                                        + "\t_LIB_NAME_.pc \\\n" + "\tdoc/Doxyfile])\n" + "AC_OUTPUT\n" + "\n"
                                        + "AC_MSG_NOTICE([\n"
                                        + "======================================================================\n"
                                        + "Configuration complete\n" + "\n"
                                        + "You may now compile the software by running 'make'\n"
                                        + "======================================================================])\n";

    public FConfigureAC(
            FormatterCtx aInFormatterCtx,
            FileNameRule aInFileNameRule,
            Indenter aInIndenter,
            BlockFormatDirective aInHeaderFormatDirective,
            BlockFormatDirective aInCommentFormatDirective,
            boolean aInIsUserFile,
            WriteStats aInStats,
            Item aInItem)
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
        FileNameRule lFnr = new FileNameRule(
                aInFnr.getRelativePath() +  lTargetModue + "model",
                null,
                aInFnr.getFilePrefix(),
                aInFnr.getFileSuffix(),
                aInFnr.getFileExtension(),
                "configure");

        return lFnr;
    }

    public void generate()
    {
        Module lModule = (Module) getItem();
        String lModuleName = lModule.getLID().getName().toLowerCase();
        String lLibName = "lib" + lModuleName + "model";
        out.println(FORMAT.replaceAll("_MODULE_NAME_", lModuleName).replaceAll("_LIB_NAME_", lLibName));
    }

}

package genie.content.format.build.automake.cpp;

import genie.content.model.mclass.MClass;
import genie.content.model.module.Module;
import genie.engine.file.WriteStats;
import genie.engine.format.*;
import genie.engine.model.Item;

/**
 * Created by midvorki on 10/7/14.
 */
public class FAutogenSH
        extends ItemFormatterTask
{
    public static final String FORMAT = "# libopflex: a framework for developing opflex-based policy agents\n"
                                        + "# Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.\n"
                                        + "#\n"
                                        + "# This program and the accompanying materials are made available under the\n"
                                        + "# terms of the Eclipse Public License v1.0 which accompanies this distribution,\n"
                                        + "# and is available at http://www.eclipse.org/legal/epl-v10.html\n" + "#\n"
                                        + "###########\n" + "#\n"
                                        + "# This autogen script will run the autotools to generate the build\n"
                                        + "# system.  You should run this script in order to initialize a build\n"
                                        + "# immediately following a checkout.\n" + "\n"
                                        + "for LIBTOOLIZE in libtoolize glibtoolize nope; do\n"
                                        + "    $LIBTOOLIZE --version 2>&1 > /dev/null && break\n" + "done\n" + "\n"
                                        + "if [ \"x$LIBTOOLIZE\" = \"xnope\" ]; then\n" + "    echo\n"
                                        + "    echo \"You must have libtool installed to compile rocksafe.\"\n"
                                        + "    echo \"Download the appropriate package for your distribution,\"\n"
                                        + "    echo \"or get the source tarball at ftp://ftp.gnu.org/pub/gnu/\"\n"
                                        + "    exit 1\n" + "fi\n" + "\n" + "ACLOCALDIRS= \n"
                                        + "if [ -d  /usr/share/aclocal ]; then\n"
                                        + "    ACLOCALDIRS=\"-I /usr/share/aclocal\"\n" + "fi\n"
                                        + "if [ -d  /usr/local/share/aclocal ]; then\n"
                                        + "    ACLOCALDIRS=\"$ACLOCALDIRS -I /usr/local/share/aclocal\"\n" + "fi\n"
                                        + "\n" + "$LIBTOOLIZE --automake --force && \\\n"
                                        + "aclocal --force $ACLOCALDIRS && \\\n"
                                        + "autoconf --force $ACLOCALDIRS && \\\n" + "autoheader --force && \\\n"
                                        + "automake --add-missing --foreign && \\\n"
                                        + "echo \"You may now configure the software by running ./configure\" && \\\n"
                                        + "echo \"Run ./configure --help to get information on the options \" && \\\n"
                                        + "echo \"that are available.\"";

    public FAutogenSH(
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
        FileNameRule lFnr = new FileNameRule(
                aInFnr.getRelativePath() +  lTargetModue + "model",
                null,
                aInFnr.getFilePrefix(),
                aInFnr.getFileSuffix(),
                aInFnr.getFileExtension(),
                "autogen");

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

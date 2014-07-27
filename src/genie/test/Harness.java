package genie.test;

import genie.content.model.mclass.MClass;
import genie.content.model.module.Module;
import genie.content.parse.modlan.ParseRegistry;
import genie.engine.format.FormatterCtx;
import genie.engine.format.FormatterRegistry;
import genie.engine.model.*;
import genie.test.TestObj;
import genie.engine.proc.Processor;
import genie.test.mtype.TypeTest;
import modlan.utils.Strings;

import java.util.*;

/**
 * Created by midvorki on 3/10/14.
 */
public class Harness
{

    public static void main(String [ ] args)
    {
        System.out.println("\n\n\n####################################################################\n\n\n");
         fileTest();
    }

    public static void fileTest()
    {

        String lMetaPaths[][] =
                {
                        {"/Users/midvorki/code/projects/genie/MODEL/SYNTAX",".meta"},
                };

        String lPrePaths[][] =
                {
                        {"/Users/midvorki/code/projects/genie/MODEL/LOADER",".cfg"},
//                        {"/Users/midvorki/code/projects/genie/MODEL/FORMATTER",".cfg"},
                };

//        String lPaths[][] = {{"/Users/midvorki/code/projects/genie/MODEL",".mdl"}};

        FormatterCtx formatterCtx[] =
                {
                        new FormatterCtx("*", "/Users/midvorki/code/projects/genie/TEST/OUT1"),
                };

        new Processor(
            3,
            lMetaPaths,
            lPrePaths,
            ParseRegistry.init(),
            formatterCtx
            );

    }
}

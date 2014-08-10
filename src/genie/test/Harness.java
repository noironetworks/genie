package genie.test;

import genie.content.model.mclass.MClass;
import genie.content.model.module.Module;
import genie.content.parse.modlan.ParseRegistry;
import genie.engine.format.FormatterCtx;
import genie.engine.format.FormatterRegistry;
import genie.engine.model.*;
import genie.test.TestObj;
import genie.engine.proc.Processor;
import modlan.report.Severity;
import modlan.utils.Strings;

import java.util.*;

/**
 * Created by midvorki on 3/10/14.
 */
public class Harness
{

    public static void main(String [ ] args)
    {
         fileTest();
    }

    public static void fileTest()
    {
        Severity.init("/Users/midvorki/code/projects/genie");

//        Severity.WARN.report("","","","BLAH BLAH");
//        System.out.println("WHAT?");
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
            1,
            lMetaPaths,
            lPrePaths,
            ParseRegistry.init(),
            formatterCtx
            );

        Severity.end(true);
    }
}

package genie;

import genie.content.parse.modlan.ParseRegistry;
import genie.engine.format.FormatterCtx;
import genie.engine.proc.Processor;
import modlan.report.Severity;

/**
 * Created by midvorki on 3/10/14.
 */
public class Genie
{

    public static void main(String [ ] args)
    {
         fileTest();
    }

    public static void fileTest()
    {
        System.out.println("Working Directory = " +
                           System.getProperty("user.dir"));

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
                };

//        String lPaths[][] = {{"/Users/midvorki/code/projects/genie/MODEL",".mdl"}};

        new Processor(
            4,
            //lMetaPaths,
            //lPrePaths,
            ParseRegistry.init()
            );

        Severity.end(true);
    }
}

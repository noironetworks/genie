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

    public static void main(String[] args)
    {
        new Processor(
                4,
                //lMetaPaths,
                //lPrePaths,
                ParseRegistry.init(),
                args);

    }

}

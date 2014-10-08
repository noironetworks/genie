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

    /**
     * Genie main method.
     * @param args a list of arguments from command line: arguments come in [[name]=[value]] or [name] format
     */
    public static void main(String[] args)
    {
        // trigger the processing
        new Processor(
                4,
                //lMetaPaths,
                //lPrePaths,
                ParseRegistry.init(),
                args);

    }

}

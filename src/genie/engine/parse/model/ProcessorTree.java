package genie.engine.parse.model;

import genie.engine.parse.modlan.ParseDirective;
import genie.engine.parse.modlan.Processor;
import genie.engine.parse.modlan.ProcessorRegistry;

/**
 * Created by midvorki on 3/22/14.
 */
public class ProcessorTree
        extends ProcessorNode implements ProcessorRegistry
{
    public ProcessorTree()
    {
        super("model");
    }

    public Processor getRoot()
    {
        return this;
    }

    public ParseDirective beginCB(genie.engine.parse.modlan.Node aIn)
    {
        return ParseDirective.CONTINUE;
    }

    public void endCB(genie.engine.parse.modlan.Node aIn)
    {

    }

}

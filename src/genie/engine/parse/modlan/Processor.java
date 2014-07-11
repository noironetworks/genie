package genie.engine.parse.modlan;

/**
 * Created by midvorki on 3/22/14.
 */
public interface Processor
{
    public ParseDirective beginCB(Node aIn);
    public void endCB(Node aIn);
    public Processor getChild(String aInName);
}

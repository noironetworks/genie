package genie.content.parse.config;

import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.model.ProcessorNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import genie.engine.proc.Config;
import modlan.report.Severity;

/**
 * Created by midvorki on 10/8/14.
 */
public class PConfigNode
        extends ParseNode
{
    /**
     * Constructor
     */
    public PConfigNode(String aInName)
    {
        super(aInName, false);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        Config.setSyntaxRelPath(aInData.getNamedValue("syntax", null, true),aInData.getNamedValue("syntaxfiletype", ".meta", true));
        Config.setLoaderRelPath(aInData.getNamedValue("loader", null, true), aInData.getNamedValue("loaderfiletype", ".cfg", true));
        Config.setGenDestPath(aInData.getNamedValue("gendest", ".", true));
        Config.setLogDirParent(aInData.getNamedValue("logfile", ".", true));
        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE,null); // TODO
    }

    /**
     * checks if the property is supported by this node. this overrides behavior to always return true
     * @param aInName name of the property
     * @return always returns true
     */
    public boolean hasProp(String aInName)
    {
        return true;
    }

}

package genie.engine.parse.model;

import modlan.report.Severity;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import genie.engine.parse.modlan.Processor;

import java.util.TreeMap;

/**
 * Created by midvorki on 3/22/14.
 */
public abstract class ProcessorNode
        implements Processor
{
    /////////////////////////////////////////////
    // SUBCLASS IMPLEMENTED CALLBACKS
    /////////////////////////////////////////////

    public abstract ParseDirective beginCB(Node aIn);
    public abstract void endCB(Node aIn);

    /////////////////////////////////////////////
    // PROCESSING METHODS
    /////////////////////////////////////////////

    public String getName()
    {
        return name;
    }

    public ProcessorNode getChild(String aInName)
    {
        return null == children ? null : children.get(aInName);
    }

    /////////////////////////////////////////////
    // REGISTRATION AND HOUSEKEEPING
    /////////////////////////////////////////////

    public void addChild(ProcessorNode aInNode)
    {
        if (null == children)
        {
            children = new TreeMap<String, ProcessorNode>();
        }
        children.put(aInNode.getName(), aInNode);
        aInNode.addParent(this);
    }

    private void addParent(ProcessorNode aInParent)
    {
        if (null != parent)
        {
            Severity.DEATH.report(
                this.toString(),
                "add child processing node",
                "redundant attachment", "already attached to " + parent);
        }
        parent = aInParent;
    }

    protected ProcessorNode(String aInName)
    {
        name = aInName;
    }

    public String toString()
    {
        ProcessorNode lThis = this;
        StringBuilder lSb = new StringBuilder();
        lSb.append("parse-proc-node[");
        while (null != lThis)
        {
            lSb.append('/');
            lSb.append(lThis.name);
            lThis = lThis.parent;
        }
        lSb.append(']');
        return lSb.toString();
    }

    private String name;
    private TreeMap<String, ProcessorNode> children = null;
    private ProcessorNode parent = null;
}

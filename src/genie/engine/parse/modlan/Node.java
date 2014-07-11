package genie.engine.parse.modlan;

import modlan.report.Severity;

/**
 * Created by midvorki on 3/17/14.
 */
public class Node
        implements modlan.parse.Data
{
    public Node(Node aInParent, String aInName)
    {
        name = aInName;
        parent = aInParent;
        if (null != aInParent)
        {
            parent.addChild(this);
        }

    }

    public final void process(Processor aInParentProcessor)
    {
        Processor lProc = null;
        if (null != aInParentProcessor)
        {
            lProc = aInParentProcessor.getChild(name);
        }
        else
        {
            // assume it's root
            // TODO:
        }

        if (null == lProc)
        {
            Severity.DEATH.report(
                this.toString(),
                "PARSE",
                "processor can't be found",
                "\"" + name + "\" processor is not registered with parent processor: " + aInParentProcessor
                 );

            lProc = aInParentProcessor;
        }
        else
        {
            switch (lProc.beginCB(this))
            {
                case END_SUBTREE:
                case END_TREE:

                    break;

                case CONTINUE:
                default:

                    if (null != children)
                    {
                        for (java.util.LinkedList<Node> lNodes : children.values())
                        {
                            for (Node lNode : lNodes)
                            {
                                lNode.process(lProc);
                            }
                        }
                    }
            }
            lProc.endCB(this);
        }
    }

    public String getName()
    {
        return name;
    }

    private void addChild(Node aIn)
    {
        if (null == children)
        {
            children = new java.util.TreeMap<String, java.util.LinkedList<Node>>();
        }
        String lKey = null == aIn.getName() ? "default" : aIn.getName();
        java.util.LinkedList<Node> lDataList = children.get(lKey);

        if (null == lDataList)
        {
            lDataList = new java.util.LinkedList<Node>();
            children.put(lKey, lDataList);
        }

        lDataList.add(aIn);

    }

    public void addComments(java.util.Collection<String> aIn)
    {
        if (null == comments)
        {
            comments = new java.util.LinkedList<String>();
        }
        System.out.println(this + ".addComments(" + aIn + ")");
        comments.addAll(aIn);
    }

    public void addComment(String aIn)
    {
        if (null == comments)
        {
            comments = new java.util.LinkedList<String>();
        }
        System.out.println(this + ".addComments(" + aIn + ")");
        comments.add(aIn);
    }

    public void setQual(String aIn)
    {
        qual = aIn;
    }

    public void setValue(String aIn)
    {
        value = aIn;
    }

    public String toString()
    {
        StringBuilder lSb = new StringBuilder();
        toString(lSb);
        return lSb.toString();
    }

    public void toString(StringBuilder aInSb)
    {
        aInSb.append("parse-node[");
        Node lThisNode = this;
        while (null != lThisNode)
        {
            aInSb.append('/');
            aInSb.append(lThisNode.getName());
            if (null != lThisNode.qual && 0 < lThisNode.qual.length())
            {
                aInSb.append('(');
                aInSb.append(lThisNode.qual);
                aInSb.append(')');
            }
            lThisNode = lThisNode.parent;
        }
        aInSb.append(']');
    }
    private final String name;
    private String qual = null;
    private String value = null;
    private java.util.LinkedList<String> comments = null;
    private java.util.TreeMap<String, java.util.LinkedList<Node>> children = null;
    private final Node parent;
}

package genie.engine.parse.modlan;

import genie.engine.model.Item;
import genie.engine.model.Pair;
import modlan.report.Severity;
import modlan.utils.Strings;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by midvorki on 3/17/14.
 */
public class Node
        implements modlan.parse.Data
{


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DATA RETRIEVAL APIS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * name accessor: retrieves name of this node
     * @return name of this node
     */
    public String getNodeName()
    {
        return name;
    }

    /**
     * checks if this node has named vaues
     * @return true if named values exist
     */
    public boolean hasNamedValues()
    {
        return null != nvps && !nvps.isEmpty();
    }

    public boolean checkFlag(String aInName)
    {
        return (!Strings.isEmpty(aInName)) &&  hasNamedValues() && nvps.containsKey(aInName);
    }

    /**
     * named value accessor. retrieves named value by name. optionally causes death if value is not found
     * @param aInName name of the value retrieved
     * @param aInDefault default value for the value retrieved
     * @param aInIsMandatory indicates if the value is mandatory (can't be unset)
     * @return value corresponding to the name
     */
    public String getNamedValue(String aInName, String aInDefault, boolean aInIsMandatory)
    {
        String lRet = aInDefault;

        if (!Strings.isEmpty(aInName))
        {
            if (hasNamedValues())
            {
                String lThis = nvps.get(aInName);
                if (!Strings.isEmpty(lThis))
                {
                    lRet = lThis;
                }
            }
        }
        if (null == lRet && aInIsMandatory)
        {
            Severity.DEATH.report(
                    this.toString(),
                    "node named value retrieval", "value not found", "value by name " + aInName + "not found;" +
                    "no default provided.");
        }
        return lRet;
    }

    public Map<String,String> getNvps()
    {
        return nvps;
    }

    /**
     * checks if this node has any child nodes
     * @return true if this node has children, false otherwise.
     */
    public boolean hasChildren()
    {
        return null != children && !children.isEmpty();
    }

    /**
     * check if this node has children of specific type identified by passed in string
     * @param aInTypeOfChild a string identifying type of the child
     * @return  true if this node has children of given type, false otherwise
     */
    public boolean hasChildren(String aInTypeOfChild)
    {
        return hasChildren() && children.containsKey(aInTypeOfChild) && !children.get(aInTypeOfChild).isEmpty();
    }

    /**
     * children list accessor: retrieves a list of children of specific type identified by passed i string
     * @param aInTypeOfChild a string identifying type of the child
     * @return a list of children corresponding to the type described by the string passed in or null if not found.
     */
    public java.util.LinkedList<Node> getChildren(String aInTypeOfChild)
    {
        return hasChildren() ? children.get(aInTypeOfChild) : null;
    }

    /**
     * checks if the node has comments
     * @return true if there are comments for this node
     */
    public boolean hasComments()
    {
        return null != comments && !comments.isEmpty();
    }

    /**
     * comments accessor. returns comments that belong to this node.
     * @return comments collection or null if it doesn't exist.
     */
    public Collection<String> getComments()
    {
        return comments;
    }

    public String getQual()
    {
        return null == qual ? name : qual;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // INTERNAL APIS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Item getAncestorItem()
    {
        return null == parent ? null : parent.item;
    }

    /**
     * INTERNAL: DO NOT CALL EXPLICITLY!
     * this function calls user-level call-back that processes this node.
     * @param aInParentProcessor parent processor
     */
    public final void process(Processor aInParentProcessor)
    {
        //System.out.println(this + ".process(parent-proc=" + aInParentProcessor + ")");
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
        //System.out.println(this + ".process(parent-proc=" + aInParentProcessor + "): my-proc=" + lProc);

        if (null == lProc)
        {
            Severity.DEATH.report(
                this.toString(),
                "PARSE",
                "processor can't be found",
                "\"" + name + "\" processor is not registered with parent processor: " + aInParentProcessor
                 );
        }
        else
        {
            Pair<ParseDirective,Item> lRes = lProc.beginCB(this, getAncestorItem());

            this.item = (null == lRes || null == lRes.getSecond()) ?
                            getAncestorItem() : lRes.getSecond();

            //System.out.println(this + ".process(parent-proc=" + aInParentProcessor + "): item=" + this.item);

            //System.out.println(this + ": NEW ITEM: " + item + " WITH PROC: " + lProc + " of class " + lProc.getClass());

            switch ((null == lRes || null == lRes.getFirst()) ?
                            ParseDirective.CONTINUE : lRes.getFirst())
            {
                case END_SUBTREE:
                case END_TREE:

                    break;

                case CONTINUE:
                default:

                    processChildren(lProc);

            }
            lProc.endCB(this, item);
        }
    }

    private void processChildren(Processor aInProc)
    {
       // System.out.println(this + ".processChildren(parent-proc=" + aInProc + ")");

        if (null != children)
        {
            for (java.util.LinkedList<Node> lNodes : children.values())
            {
                for (Node lNode : lNodes)
                {
                    lNode.process(aInProc);
                }
            }
        }
    }
    /**
     * INTERNAL: DO NOT CALL EXPLICITLY!
     * add a child node to this node.
     * @param aIn child node
     */
    private void addChild(Node aIn)
    {
        if (null == children)
        {
            children = new java.util.TreeMap<String, java.util.LinkedList<Node>>();
        }

        String lKey = null == aIn.getNodeName() ? "default" : aIn.getNodeName();
        java.util.LinkedList<Node> lDataList = children.get(lKey);

        if (null == lDataList)
        {
            lDataList = new java.util.LinkedList<Node>();
            children.put(lKey, lDataList);
        }

        lDataList.add(aIn);

    }

    /**
     * INTERNAL: DO NOT CALL EXPLICITLY!
     * adds a list of comments to this node.
     * @param aIn list of comments to be added
     */
    public void addComments(java.util.Collection<String> aIn)
    {
        if (null == comments)
        {
            comments = new java.util.LinkedList<String>();
        }
        //System.out.println(this + ".addComments(" + aIn + ")");
        comments.addAll(aIn);
    }

    /**
     * INTERNAL: DO NOT CALL EXPLICITLY!
     * adds a single comment line to this node.
     * @param aIn a comment line to be added.
     */
    public void addComment(String aIn)
    {
        if (null == comments)
        {
            comments = new java.util.LinkedList<String>();
        }
        //System.out.println(this + ".addComments(" + aIn + ")");
        comments.add(aIn);
    }

    /**
     * INTERNAL: DO NOT CALL EXPLICITLY!
     * node qualifier mutator. sets this node's qualifier
     * @param aIn qualifier
     */
    public void setQual(String aIn)
    {
        qual = processComplex("qual",aIn);
    }

    /**
     * INTERNAL: DO NOT CALL EXPLICITLY!
     * node vlaue mutator. sets this node's value
     * @param aIn value passed in
     */
    public void setValue(String aIn)
    {
        value = aIn;
    }

    /**
     * INTERNAL: DO NOT CALL EXPLICITLY!
     * Constructor
     * @param aInParent parent node
     * @param aInName name of this node
     */
    public Node(Node aInParent, String aInName)
    {
        name = aInName;
        parent = aInParent;
        if (null != aInParent)
        {
            parent.addChild(this);
        }

    }

    /**
     * INTERNAL: DO NOT CALL EXPLICITLY!
     * complex value string processor
     * @param aInType type of the node
     * @param aIn string to be treated.
     * @return identifying string
     */
    private String processComplex(String aInType,String aIn)
    {
        String lRet = null;
        String[] lComponents = aIn.split(";");

        lRet = lComponents[0];

        for (int i = 0; i < lComponents.length; i++)
        {
            String lComponent = lComponents[i];
            String lNVPair[] = lComponent.split(":|=");

            for (int j = 0; j < lNVPair.length; j++)
            {
                lNVPair[j] = lNVPair[j].trim();
            }
            switch (lNVPair.length)
            {
                case 1:

                    if (0 == i)
                    {
                        addNVP(aInType, lNVPair[0]); // NO VALUE
                        addNVP("name", lNVPair[0]); // NO VALUE
                    }
                    addNVP(lNVPair[0], lNVPair[0]); // NO VALUE
                    break;

                case 2:

                    if (0 == i)
                    {
                        addNVP(aInType, lNVPair[0]); // NO VALUE
                    }
                    addNVP(lNVPair[0], lNVPair[1]);
                    break;

                default:

                    Severity.DEATH.report(
                            this.toString(),
                            "processing of complex value",
                            "bad " + aInType + "format",
                            "name value pair can only have one value" + aIn);

            }
        }
        return lRet;
    }

    /**
     * INTERNAL: DO NOT CALL EXPLICITLY!
     * add name value pair
     * @param aInKey name/key
     * @param aInValue value
     */
    private void addNVP(String aInKey, String aInValue)
    {
        if (null == nvps)
        {
            nvps = new TreeMap<String, String>();
        }
        nvps.put(aInKey,aInValue);
    }

    /**
     * stringifier
     * @return readable form of this node object
     */
    public String toString()
    {
        StringBuilder lSb = new StringBuilder();
        toString(lSb);
        return lSb.toString();
    }

    /**
     * stringifier
     * @param aInSb readable form of this node object
     */
    public void toString(StringBuilder aInSb)
    {
        aInSb.append("parse-node[");
        Node lThisNode = this;
        while (null != lThisNode)
        {
            aInSb.append('/');
            aInSb.append(lThisNode.getNodeName());
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
    private Map<String,String> nvps = null;
    private java.util.LinkedList<String> comments = null;
    private java.util.TreeMap<String, java.util.LinkedList<Node>> children = null;
    private final Node parent;
    private Item item = null;
}

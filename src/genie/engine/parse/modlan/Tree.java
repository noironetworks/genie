package genie.engine.parse.modlan;

import modlan.parse.Consumer;

import java.util.LinkedList;

/**
 * Created by midvorki on 3/15/14.
 */
public class Tree
        implements Consumer
{
    public Tree(ProcessorRegistry aInPreg)
    {
        preg = aInPreg;
    }

    private void process()
    {
        // TODO:
        if (null != root)
        {
            root.process(preg.getRoot());
        }
    }

    //////////////////////////////////////////////////////////////
    // MODLAN PARSINNG CALLBACKS /////////////////////////////////
    //////////////////////////////////////////////////////////////

    public modlan.parse.Data onDocBegin(String aInName)
    {
        System.out.println("\n\n---------> [" + root + "] DOC:BEGIN=" + aInName);
        stack.push(root);
        return root;
    }
    public modlan.parse.Data onDocEnd(String aInName)
    {
        System.out.println("\n\n---------> [" + root + "]  DOC:END=" + aInName);
        stack.pop();
        process();
        return root;
    }

    public modlan.parse.Data onNodeBegin(String aInName)
    {

        Node lData = new Node(stack.peek(), aInName);
        stack.push(lData);
        lData.addComments(commentBuffer);
        commentBuffer.clear();
        System.out.println("\n\n--------->  [" + lData + "] NODE:BEGIN=" + lData);
        return lData;
    }
    public modlan.parse.Data onNodeEnd(String aInName)
    {
        Node lData = stack.peek();
        lData.addComments(commentBuffer);
        commentBuffer.clear();
        stack.pop();

        System.out.println("\n\n--------->  [" + lData + "] NODE:END=" + lData);
        return lData;
    }

    public modlan.parse.Data onQual(String aIn)
    {
        Node lData = stack.peek();
        lData.setQual(aIn);
        System.out.println("\n\n--------->  [" + lData + "] QUAL=" + aIn + " IN " + lData);
        return lData;
    }

    public modlan.parse.Data onComment(String aInLine)
    {
        System.out.println("\n\n--------->  [...] COMMENT=" + aInLine);
        commentBuffer.add(aInLine);
        return null;
    }

    public modlan.parse.Data onText(String aInLine)
    {
        Node lData = stack.peek();
        lData.setValue(aInLine);
        System.out.println("\n\n--------->  [" + lData + "] TEXT=" + aInLine + " IN " + lData);
        return lData;
    }

    public modlan.parse.Data onValue(String aInValue)
    {
        Node lData = stack.peek();
        lData.setValue(aInValue);
        System.out.println("\n\n--------->  [" + lData + "] VALUE=" + aInValue + " IN " + lData);
        return lData;
    }

    public modlan.parse.Data onContentBegin(String aInName)
    {
        System.out.println("\n\n--------->  [...] CONTENT:BEGIN=" + aInName);
        stack.peek().addComments(commentBuffer);
        commentBuffer.clear();
        return stack.peek();
    }

    public modlan.parse.Data onContentEnd(String aInName)
    {
        System.out.println("\n\n--------->  [...] CONTENT:END=" + aInName);
        stack.peek().addComments(commentBuffer);
        commentBuffer.clear();
        return stack.peek();
    }

    //////////////////////////////////////////////////////////////
    // PRIVATE DATA //////////////////////////////////////////////
    //////////////////////////////////////////////////////////////


    private final java.util.Stack<Node> stack = new java.util.Stack<Node>();
    private final Node root = new Node(null, "doc-root");
    private final LinkedList<String> commentBuffer = new LinkedList<String>();
    private final ProcessorRegistry preg;
}

package genie.content.parse.pmeta;

import genie.content.model.mmeta.MNode;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

import java.io.StringWriter;

/**
 * Created by midvorki on 7/16/14.
 *
 * This is a parsing node for meta abstraction for representing loadable parsing nodes.
 * Parsing nodes are registered in corresponding STRUCT.meta file that represents the
 * meta-structure of the parsed tree.
 */
public class PNode
        extends ParseNode
{
    /**
     * Constructor
     */
    public PNode()
    {
        super("node", true);
    }

    /**
     * Parsing callback to indicate beginning of the node parsing. This callback creates a parsing metadata item that is
     * later used in forming the parse tree for model loading.
     *
     * @param aInData parsed data node
     * @param aInParentItem parent item that was a result of parsing parent nodes. can be null if no parent.
     * @return a pair of resulting parse directive and the item that was produced as result of parsing this data node.
     */
    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        // CREATE META NODE THAT CORRESPONDS TO THIS NODE PARSING RULE
        MNode lMNode = new MNode(aInParentItem, aInData.getQual());

        // CHECK IF WE HAVE TO USE OTHER NODE'S PARSER
        String lUse = aInData.getNamedValue("use", null, false);
        if (!Strings.isEmpty(lUse))
        {
            lMNode.addUses(lUse);
        }
        else
        {
            // GET THE PARSE NODE'S EXPLICIT PARSING CLASS DIRECTIVE
            String lExplicit = aInData.getNamedValue("explicit", null, false);

            // CHECK IF DIRECT PARSING CLASS DIRECTIVE IS SET
            String lParserImplClassName = null;
            if (!Strings.isEmpty(lExplicit))
            {
                lParserImplClassName = lExplicit;
            }
            // CHECK IF THIS THIS IS A GENERIC/PLACEHOLDER NODE
            else if (aInData.checkFlag("generic"))
            {
                lParserImplClassName = "genie.engine.parse.model.ParseNode";
            }
            // THIS IS AN AUTO-PICKED PARSING NODE... LET'S FABRICATE THE NAME
            else
            {
                // DETERMINE DEFAULT PARSER NAME FOR THIS NODE
                String lQual = aInData.getQual();
                String lNs = aInData.getNamedValue("namespace", lQual, false);
                StringWriter lParserImplClassNameBuff = new StringWriter();
                lParserImplClassNameBuff.append("genie.content.parse.p");
                lParserImplClassNameBuff.append(lNs.toLowerCase());
                lParserImplClassNameBuff.append(".P");
                lParserImplClassNameBuff.append(Strings.upFirstLetter(lQual));
                lParserImplClassNameBuff.append("Node");
                lParserImplClassName = lParserImplClassNameBuff.toString();
            }
            // Set the parser node class name
            lMNode.addExplicitParserClassName(lParserImplClassName);
        }
        return new Pair<ParseDirective, Item>(
                ParseDirective.CONTINUE,
                lMNode);
    }
}

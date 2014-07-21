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
 */
public class PNode
        extends ParseNode
{
    public PNode()
    {
        super("node", true);
    }

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
            else if (aInData.checkFlag("generic"))
            {
                lParserImplClassName = "genie.engine.parse.model.ParseNode";
            }
            else
            {
                // DETERMINE DEFAULT PARSER NAME FOR THIS NODE
                StringWriter lParserImplClassNameBuff = new StringWriter();
                lParserImplClassNameBuff.append("genie.content.parse.p");
                String lQual = aInData.getQual();
                lParserImplClassNameBuff.append(lQual.toLowerCase());
                lParserImplClassNameBuff.append(".P");
                lParserImplClassNameBuff.append(Strings.upFirstLetter(lQual));
                lParserImplClassNameBuff.append("Node");
                lParserImplClassName = lParserImplClassNameBuff.toString();
            }
            lMNode.addExplicitParserClassName(lParserImplClassName);
        }
        return new Pair<ParseDirective, Item>(
                ParseDirective.CONTINUE,
                lMNode);
    }
}

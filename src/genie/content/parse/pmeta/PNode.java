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
        System.out.println("\n\n---------------> " + this + ".beginCb(" + aInData + ", " + aInParentItem + "): " + aInData.getNvps());

        System.out.println("---------------> explicit: " + lExplicit + "; use: " + lUse);

        ParseNode lParentParseNode =
                (null != aInParentItem && aInParentItem instanceof MNode) ?
                    (ParseNode)((MNode) aInParentItem).getContext() :
                    null;

        ParseNode lParseNode = new ParseNode(aInData.getQual(),false);

        if (null != lParentParseNode)
        {
            lParentParseNode.addChild(lParseNode);
        }


        MNode lMNode = new MNode(aInParentItem, aInData.getQual(), null);
        String lExplicit = aInData.getNamedValue("explicit", null, false);
        String lUse = aInData.getNamedValue("use", null, false);

        String lParserImplClassName = null;
        if (!Strings.isEmpty(lExplicit))
        {
            lParserImplClassName = lExplicit;
        }
        else
        {
            StringWriter lParserImplClassNameBuff = new StringWriter();
            lParserImplClassNameBuff.append("");
            if (!Strings.isEmpty(lUse))
            {
                lMNode.addUses(lUse);
            }
            if (!Strings.isEmpty(lExplicit))
            {

            }
        }
        System.out.println("--------------- END::: " + aInData + ": mnode=" + lMNode + " under parent: " + aInParentItem + "\n\n");

        return new Pair<ParseDirective, Item>(
                ParseDirective.CONTINUE,
                lMNode);
    }
}

package genie.content.format.meta.mdl;

import genie.content.model.mmeta.MNode;
import genie.content.model.mmeta.MNodeProp;
import genie.engine.file.WriteStats;
import genie.engine.format.*;
import genie.engine.model.Item;

import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by midvorki on 7/25/14.
 */
public class FMetaDef
        extends GenericFormatterTask
{
    public FMetaDef(
            FormatterCtx aInFormatterCtx, FileNameRule aInFileNameRule, Indenter aInIndenter, BlockFormatDirective aInHeaderFormatDirective, BlockFormatDirective aInCommentFormatDirective, String aInName, boolean aInIsUserFile, WriteStats aInStats
                   )
    {
        super(aInFormatterCtx,
              aInFileNameRule,
              aInIndenter,
              aInHeaderFormatDirective,
              aInCommentFormatDirective,
              aInName,
              aInIsUserFile,
              aInStats);
    }

    public void generate()
    {
        System.out.println("--------->" + this + ".generate()");
        out.println("Hi, I really really like you");
        genNodelist(0, null, MNode.MY_CAT.getNodes().getItemsList());
    }

    private void genNodelist(int aInIndent, Item aInParent, Collection<Item> aInCol)
    {
        for (Item lItem : aInCol)
        {
            MNode lNode = (MNode) lItem;
            genNode(aInIndent,aInParent, lNode);
        }
    }
    private void genNode(int aInIndent, Item aInParent, MNode aInNode)
    {
        if (aInNode.getParent() == aInParent)
        {
            out.println(aInIndent, aInNode.getLID().getName());
            genProps(aInIndent, aInNode);

            LinkedList<Item> lChildNodes = new LinkedList<Item>();
            aInNode.getChildItems(MNode.MY_CAT, lChildNodes);

            if (!lChildNodes.isEmpty())
            {
                out.println(aInIndent, '{');
                genNodelist(aInIndent + 1, aInNode, lChildNodes);
                out.println(aInIndent, '}');
            }
        }
    }
    private void genProps(int aInIndent, MNode aInNode)
    {
        TreeMap<String, MNodeProp> lNodeProps = new TreeMap<String, MNodeProp>();
        aInNode.getProps(lNodeProps);
        if (!lNodeProps.isEmpty())
        {
            out.println(aInIndent, '[');
            genNodeProps(aInIndent, lNodeProps.values());
            out.println(aInIndent, ']');
        }
    }
    private void genNodeProps(int aInIndent, Collection<MNodeProp> aInNodeProps)
    {
        for (Item lItem : aInNodeProps)
        {
            MNodeProp lProp = (MNodeProp) lItem;
            genNodeProp(aInIndent + 1, lProp);
        }
    }

    private void genNodeProp(int aInIndent, MNodeProp aInProp)
    {
        switch (aInProp.getType())
        {
            case OPTION:

                out.println(aInIndent, aInProp.getLID().getName() + ";");
                break;

            default:

                out.println(aInIndent, aInProp.getLID().getName() + "=<" + aInProp.getLID().getName().toUpperCase() + ">;");
                break;
        }
    }
}

package genie.content.model.mmeta;

import genie.content.model.module.Module;
import genie.engine.model.*;
import genie.engine.parse.model.ParseNode;
import genie.engine.proc.Processor;
import modlan.report.Severity;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/16/14.
 */
public class MNode extends Item
{
    public static final Cat MY_CAT = Cat.getCreate("parser:meta");
    public static final RelatorCat USES_REL = RelatorCat.getCreate("parse:meta:uses", Cardinality.SINGLE);

    public MNode(Item aInParent, String aInLName)
    {
        super(MY_CAT, aInParent, aInLName);
    }

    public void addUses(String aInTargetGName)
    {
        USES_REL.add(MY_CAT, getGID().getName(), MY_CAT, aInTargetGName);
    }

    public boolean usesOtherNode()
    {
        Relator lRel = USES_REL.getRelator(getGID().getName());
        return null != lRel && lRel.hasTo();
    }

    public Relator getUsesRelator()
    {
        return USES_REL.getRelator(getGID().getName());
    }

    public MNode getUsesNode()
    {
        Relator lRel = getUsesRelator();
        return (MNode) (null == lRel ? null : lRel.getToItem());
    }

    private Class getParserClass()
    {
        if (null == parserClass)
        {
            if (Strings.isEmpty(parserClassName))
            {
                if (usesOtherNode())
                {
                    MNode lUsesNode = getUsesNode();
                    if (null != lUsesNode)
                    {
                        parserClass = lUsesNode.getParserClass();
                    }
                    else
                    {
                        Severity.DEATH.report(
                                toString(),
                                "parser class retrieval",
                                "no parser class defined",
                                "can't resolve indirection: " +
                                 getUsesRelator().getToRelator().getItemGName() +
                                 "; RELATOR: " + getUsesRelator() +
                                 "; TARGET RELATOR: " + getUsesRelator().getToRelator());
                    }
                }
                else
                {
                    Severity.DEATH.report(
                            toString(),
                            "parser class retrieval",
                            "no parser class defined",
                            "no explicit or indirect definition: uses other node: " +
                            usesOtherNode() +
                            "; explicit: " + parserClassName);
                }
            }
            else
            {
                try
                {
                    parserClass = ClassLoader.getSystemClassLoader().loadClass(parserClassName);
                }
                catch (Throwable lE)
                {
                    Severity.DEATH.report(
                            toString(),
                            "parser class retriever",
                            "no such parser class: " + parserClassName +
                            " uses other node: " +
                            usesOtherNode(),
                            lE);
                }
            }
        }
        return parserClass;
    }

    public void addExplicitParserClassName(String aIn)
    {
        parserClassName = aIn;
    }

    public Object getContext()
    {
        return context;
    }

    public void metaModelLoadCompleteCb()
    {
        getParseNode();
    }

    public ParseNode getParseNode()
    {
        if (null == context)
        {
            Item lParentItem = getParent();
            ParseNode lParentParseNode = null;
            if (null == lParentItem)
            {
                System.out.println(":::: NULL PARENT");
                lParentParseNode = Processor.get().getPTree().getDocRoot();
            }
            else if (lParentItem instanceof MNode)
            {
                System.out.println(":::: MNode PARENT: " + lParentItem);
                lParentParseNode = ((MNode) lParentItem).getParseNode();
            }
            else
            {
                Severity.DEATH.report(this.toString(), "parsing rule structure formation", "unexpected structure",
                                      "can't be parented by " + lParentItem);
            }

            Class lParserClass = getParserClass();
            try
            {
                ParseNode lParseNode = (ParseNode) lParserClass.getConstructors()[0].newInstance(getLID().getName());
                lParentParseNode.addChild(lParseNode);
                System.out.println("**************> " + this + ".getParseNode(): PARSE NODE:" + lParseNode);
                context = lParseNode;
            }
            catch (Throwable lE)
            {
                Severity.DEATH.report(this.toString(),"parsing rule structure formation", "can't invoke constructor for: " + parserClassName, lE);
            }
        }
        return context;
    }

    private ParseNode context = null;
    private String parserClassName = null;
    private Class<?> parserClass = null;
}

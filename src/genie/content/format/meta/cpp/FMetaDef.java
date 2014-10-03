package genie.content.format.meta.cpp;

import genie.content.model.mclass.MClass;
import genie.content.model.mnaming.MNameComponent;
import genie.content.model.mnaming.MNameRule;
import genie.content.model.mnaming.MNamer;
import genie.content.model.mownership.MOwner;
import genie.content.model.mprop.MProp;
import genie.content.model.mrelator.MRelationshipClass;
import genie.content.model.mtype.MType;
import genie.content.model.mtype.MTypeHint;
import genie.engine.file.WriteStats;
import genie.engine.format.*;
import genie.engine.model.Ident;
import genie.engine.model.Item;
import modlan.utils.Strings;

import java.util.Collection;
import java.util.TreeMap;

/**
 * Created by midvorki on 9/24/14.
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
        out.println(0, "namespace opflex");
        out.println(0, "{");
        out.println(1, "namespace modb");
        out.println(1, "{");

        out.printHeaderComment(
                2,
                new String[]
                        {
                                "A base fixture that defines a simple object model"
                        });
        out.println(2, "class MDFixture");
        out.println(2, "{");
        out.println(3, "public:");
        out.println();
        out.println(4, "MDFixture() :");
        out.println(5, "md(");
        out.println(6, "list_of");
        //MClass lRoot = MClass.getContainmentRoot();
        for (Item lIt : MClass.MY_CAT.getNodes().getItemsList())
        {
            MClass lClass = (MClass) lIt;
            //if (lClass.isConcrete())
            {
                genMo(7, lClass);
            }
        }
        out.println(5, "  ) // md");
        out.println(4, "{");
        out.println(4, "}");
        out.println();
        out.println(4, "ModelMetadata md;");
        out.println(3, "private:");

        out.println(2, "}; // MDFixture");
        out.println(1, "} // namespace modb");

        out.println(0, "} // namespace opflex");
    }

    public static String getClassType(MClass aIn)
    {
        if (isPolicy(aIn))
        {
            return "ClassInfo::POLICY";
        }
        else if (isObservable(aIn))
        {
            return "ClassInfo::OBSERVABLE";
        }
        else if (isRelationshipSource(aIn))
        {
            return "ClassInfo::RELATIONSHIP";
        }
        else if (isRelationshipTarget(aIn))
        {
            return "ClassInfo::INVERSE_RELATIONSHIP";
        }
        else  if (isRelationshipResolver(aIn))
        {
            return "ClassInfo::RESOLVER";
        }
        else if (!aIn.isConcrete())
        {
            return "ClassInfo::ABSTRACT";
        }
        else
        {
            return "ClassInfo::LOCAL_ONLY";
        }
    }

    public static boolean isPolicy(MClass aIn)
    {
        return aIn.isSubclassOf("policy/Component") || aIn.isSubclassOf("policy/Definition");
    }

    public static boolean isObservable(MClass aIn)
    {
        return aIn.isSubclassOf("observer/Component") || aIn.isSubclassOf("observer/Definition"); //TODO: WHAT SHOULD THESE CLASSES BE?
    }

    public static boolean isRelationshipSource(MClass aIn)
    {
        return aIn.isConcreteSuperclassOf("relator/Source");
    }

    public static boolean isRelationshipTarget(MClass aIn)
    {
        return aIn.isConcreteSuperclassOf("relator/Target");
    }

    public static boolean isRelationshipResolver(MClass aIn)
    {
        return aIn.isConcreteSuperclassOf("relator/Resolver");
    }

    public static String getOwner(MClass aIn)
    {
        Collection<MOwner> lOwners = aIn.findOwners();
        return lOwners.isEmpty() ? (aIn.isConcrete() ? "default" : "abstract") : lOwners.iterator().next().getLID().getName();
    }

    private void genMo(int aInIndent, MClass aInClass)
    {
        out.println(aInIndent, '(');
            if (aInClass instanceof MRelationshipClass)
            {
                out.printIncodeComment(aInIndent + 1, new String[]
                        {
                                "RELATIONSHIPS: " + ((MRelationshipClass) aInClass).getRelationships(),
                                "NAME: " + aInClass.getGID().getName(),
                                "CONTAINED BY: " + aInClass.getContainedByClasses(true, true),
                                "SOURCE: " + ((MRelationshipClass) aInClass).getSourceClass().getGID().getName(),
                                "TARGETS: " + ((MRelationshipClass) aInClass).getTargetClasses(),
                        });
            }
            out.print(aInIndent + 1, "ClassInfo(" + aInClass.getGID().getId() + ", ");
            if (aInClass.hasSuperclass())
            {
                MClass lSuper = aInClass.getSuperclass();
                out.print(lSuper.getGID().getId() + "/* super: " + lSuper.getGID().getName() + " */, ");
            }
            else
            {
                out.print("0 /* no superclass */, ");
            }
            out.println(getClassType(aInClass) + ", \"" + aInClass.getFullConcatenatedName() + "\", \"" + getOwner(aInClass) + "\",");

                genProps(aInIndent + 2, aInClass);
                genNamingProps(aInIndent + 2, aInClass);
                out.println(aInIndent + 2, ")");
        out.println(aInIndent, ')');
    }

    private void genProps(int aInIndent, MClass aInClass)
    {
        //boolean hasDesc = aInClass.hasProps() || aInClass.hasContained();
        TreeMap<String,MProp> lProps = new TreeMap<String, MProp>();
        // aInClass.findProp(lProps,false);
        aInClass.getProp(lProps, true);

        TreeMap<Ident,MClass> lConts = new TreeMap<Ident, MClass>();
        aInClass.getContainsClasses(lConts, false, true);//true, true);

        if (lProps.size() + lConts.size() == 0)
        {
            out.println(aInIndent, "std::vector<PropertyInfo>(),");
        }
        else
        {
//            int lCount = 0;
            out.println(aInIndent, "list_of");
            // HANLDE PROPS
                for (MProp lProp : lProps.values())
                {
                    MType lPropType = lProp.getType(false);
                    MType lPrimitiveType = lPropType.getBuiltInType();
                    MTypeHint lHint = lPrimitiveType.getTypeHint();

                    int lLocalId = lProp.getLocalIdx();

                    if ((lProp.getLID().getName().equalsIgnoreCase("targetName") ||
                         lProp.getLID().getName().equalsIgnoreCase("targetClass")) &&
                        isRelationshipSource(aInClass))
                    {
                        out.print(
                                aInIndent + 1,
                                "(PropertyInfo(" + lLocalId + ", \"" + lProp.getLID().getName() + "\", PropertyInfo::" + lPrimitiveType.getLID().getName().toUpperCase() + ", ");

                        out.print("list_of");
                        for (MClass lTargetClass : ((MRelationshipClass) aInClass).getTargetClasses())
                        {
                            out.print("(" + lTargetClass.getGID().getId() + "/*" + lTargetClass.getGID().getName() + "*/)");
                        }
                        out.println(", PropertyInfo::SCALAR)) // " + lProp.toString());
                    }
                    else if (lProp.getLID().getName().equalsIgnoreCase("source") && isRelationshipTarget(aInClass))
                    {
                        MClass lTargetClass = ((MRelationshipClass) aInClass).getSourceClass();
                        out.println(
                                aInIndent + 1,
                                "(PropertyInfo(" + lLocalId + ", \"" + lProp.getLID().getName() + "\", PropertyInfo::" + lPrimitiveType.getLID().getName().toUpperCase() + ", " + lTargetClass.getGID().getId() + "/* " + lTargetClass.getGID().getName() + "*/, PropertyInfo::SCALAR)) // "
                                + lProp.toString());
                    }
                    // TODO
                    /**else if (isRelationshipResolver(aInClass))
                    {

                    }
                     **/
                    else
                    {
                        out.println(
                                aInIndent + 1,
                                "(PropertyInfo(" + lLocalId + ", \"" + lProp.getLID().getName() + "\", PropertyInfo::" + lPrimitiveType.getLID().getName().toUpperCase() + ", PropertyInfo::SCALAR)) // "
                                + lProp.toString());
                    }
                }


            // HANDLE CONTAINED CLASSES
                for (MClass lContained : lConts.values())
                {
                    out.println(aInIndent + 1, "(PropertyInfo(" + (1000 + lContained.getGID().getId()) + ", \"" + lContained.getFullConcatenatedName() + "\", PropertyInfo::COMPOSITE, " + lContained.getGID().getId() + ")) // " + lContained.toString());
                }

                out.println(aInIndent + 1, ",");
        }
    }

    private MNameRule getNamingRule(MClass aInClass)
    {
        Collection<MNameRule> lNrs = aInClass.findNamingRules();
        return lNrs.isEmpty() ? null : lNrs.iterator().next();
    }

    private void genNamingProps(int aInIndent, MClass aInClass)
    {
        MNameRule lNr = getNamingRule(aInClass);

        if (null == lNr)
        {
            out.println(aInIndent, "std::vector<PropertyInfo>() // no naming rule; assume cardinality of 1 in any containment rule");
        }
        else
        {
            Collection<MNameComponent> lComps = lNr.getComponents();
            int lNamePropsCount = 0;
            for (MNameComponent lIt : lComps)
            {
                if (lIt.hasPropName())
                {
                    lNamePropsCount++;
                }
            }
            if (0 == lNamePropsCount)
            {
                out.println(aInIndent, "std::vector<PropertyInfo>() // no naming props in rule " + lNr + "; assume cardinality of 1");
            }
            else
            {
                out.println(aInIndent, "list_of // " + lNr);
                for (MNameComponent lIt : lComps)
                {
                    if (lIt.hasPropName())
                    {
                        MProp lProp = aInClass.findProp(lIt.getPropName(),false);
                        if (null != lProp)
                        {
                            out.println(aInIndent + 1, "(" + lProp.getLocalIdx() + ") //" + lProp + " of name component " + lIt);
                        }
                    }
                }
            }
        }
    }
    //private TreeMap<String, Integer> propIds = new TreeMap<String, Integer>();
}

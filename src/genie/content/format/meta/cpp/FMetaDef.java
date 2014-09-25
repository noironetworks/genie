package genie.content.format.meta.cpp;

import genie.content.model.mclass.MClass;
import genie.content.model.mprop.MProp;
import genie.content.model.mtype.MType;
import genie.content.model.mtype.MTypeHint;
import genie.engine.file.WriteStats;
import genie.engine.format.*;
import genie.engine.model.Ident;
import genie.engine.model.Item;

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
            if (lClass.isConcrete())
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

    private void genMo(int aInIndent, MClass aInClass)
    {
        out.println(aInIndent, '(');
            out.println(aInIndent + 1, "ClassInfo(" + aInClass.getGID().getId() + ", " + getClassType(aInClass) + ", \"" + aInClass.getFullConcatenatedName() + "\", \"" + aInClass.getOwnerName() + "\",");
                genProps(aInIndent + 2, aInClass);
                genNamingProps(aInIndent + 2, aInClass);
                out.println(aInIndent + 2, ")");
        out.println(aInIndent, ')');
    }

    private void genProps(int aInIndent, MClass aInClass)
    {
        //boolean hasDesc = aInClass.hasProps() || aInClass.hasContained();
        TreeMap<String,MProp> lProps = new TreeMap<String, MProp>();
        aInClass.findProp(lProps,false);

        TreeMap<Ident,MClass> lConts = new TreeMap<Ident, MClass>();
        aInClass.getContainsClasses(lConts, true, true);

        if (lProps.size() + lConts.size() == 0)
        {
            out.println(aInIndent, "std::vector<PropertyInfo>(),");
        }
        else
        {
            int lCount = 0;
            out.println(aInIndent, "list_of");
            // HANLDE PROPS
                for (MProp lProp : lProps.values())
                {
                    MType lPropType = lProp.getType(false);
                    MType lPrimitiveType = lPropType.getBuiltInType();
                    MTypeHint lHint = lPrimitiveType.getTypeHint();

                    out.println(aInIndent + 1, "(PropertyInfo(" + (++lCount) + ", \"" + lProp.getLID().getName() + "\", PropertyInfo::" + lPrimitiveType.getLID().getName().toUpperCase() + ", PropertyInfo::" + lHint.getInfo().toString() + ")) // " + lProp.toString());
                }


            // HANDLE CONTAINED CLASSES
                for (MClass lContained : lConts.values())
                {
                    out.println(aInIndent + 1, "(PropertyInfo(" + (++lCount) + ", \"" + lContained.getFullConcatenatedName() + "\", PropertyInfo::COMPOSITE, " + lContained.getGID().getId() + ")) // " + lContained.toString());
                }

                out.println(aInIndent + 1, ",");
        }
    }

    private void genNamingProps(int aInIndent, MClass aInClass)
    {

    }
}

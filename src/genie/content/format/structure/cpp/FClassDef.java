package genie.content.format.structure.cpp;

import genie.content.model.mclass.MClass;
import genie.engine.file.WriteStats;
import genie.engine.format.*;
import genie.engine.model.Ident;
import genie.engine.model.Item;
import modlan.report.Severity;

import java.util.TreeMap;

/**
 * Created by midvorki on 9/28/14.
 */
public class FClassDef extends ItemFormatterTask
{
    public FClassDef(
            FormatterCtx aInFormatterCtx,
            FileNameRule aInFileNameRule,
            Indenter aInIndenter,
            BlockFormatDirective aInHeaderFormatDirective,
            BlockFormatDirective aInCommentFormatDirective,
            String aInName,
            boolean aInIsUserFile,
            WriteStats aInStats,
            Item aInItem)
    {
        super(
             aInFormatterCtx,
             aInFileNameRule,
             aInIndenter,
             aInHeaderFormatDirective,
             aInCommentFormatDirective,
             aInName,
             aInIsUserFile,
             aInStats,
             aInItem);
    }

    public void generate()
    {
        Severity.WARN.report("","","","GENERATING: " + getItem());
        MClass lClass = (MClass) getItem();
        generate(0, lClass);
    }

    public static String getClassName(MClass aInClass, boolean aInFullyQualified)
    {
        return aInFullyQualified ? (getNamespace(aInClass,true) + "::" + aInClass.getLID().getName()) : aInClass.getLID().getName();
    }

    public static String getInclTag(MClass aInClass)
    {
        return "GI_" + aInClass.getModule().getLID().getName().toUpperCase() + '_' + aInClass.getLID().getName().toUpperCase() + "_HPP";
    }

    public static String getNamespace(String aInModuleName, boolean aInFullyQualified)
    {
        return aInFullyQualified ? ("opflex::" + aInModuleName) : (aInModuleName);
    }

    public static String getNamespace(MClass aInClass, boolean aInFullyQualified)
    {
        return getNamespace(aInClass.getModule().getLID().getName(), aInFullyQualified);
    }

    public static String getInclude(String aInPath, boolean aInIsBuiltIn)
    {
        return  "#include " + (aInIsBuiltIn ? '<' : '\"') + aInPath + (aInIsBuiltIn ? '>' : '\"');
    }

    public static String getIncludePath(MClass aIn)
    {
        return getNamespace(aIn,false) + "/" + aIn.getLID().getName();
    }

    public static String getInclude(MClass aIn)
    {
        return getInclude(getIncludePath(aIn) + ".hpp", false);
    }
    private void generate(int aInIndent, MClass aInClass)
    {
        String lInclTag = getInclTag(aInClass);
        out.println(aInIndent,"#ifndef " + lInclTag);
        out.println(aInIndent,"#define " + lInclTag);
        genForwardDecls(aInIndent, aInClass);
        genIncludes(aInIndent, aInClass);
        genBody(aInIndent, aInClass);
        out.println(aInIndent,"#endif // " + lInclTag);

    }

    private void genForwardDecls(int aInIndent, MClass aInClass)
    {
        out.println();
        out.println(aInIndent, "namespace opflex");
        out.println(aInIndent, "{");
            out.printIncodeComment(aInIndent + 1, "FORWARD DECLARATIONS FOR INHERITANCE ");
            for (MClass lThis = aInClass; null != lThis; lThis = lThis.getSuperclass())
            {
                genForwardDecl(aInIndent + 1,lThis);
            }
            TreeMap<Ident, MClass> lConts = new TreeMap<Ident, MClass>();
            aInClass.getContainsClasses(lConts, true, true);
            out.printIncodeComment(aInIndent + 1, "FORWARD DECLARATIONS FOR CONTAINED ");
            for (MClass lThis : lConts.values())
            {
                genForwardDecl(aInIndent + 1,lThis);
            }
            TreeMap<Ident, MClass> lContr = new TreeMap<Ident, MClass>();
            aInClass.getContainedByClasses(lContr, true, true);
            out.printIncodeComment(aInIndent + 1, "FORWARD DECLARATIONS FOR CONTAINERS ");
            for (MClass lThis : lContr.values())
            {
                genForwardDecl(aInIndent + 1,lThis);
            }
        out.println(aInIndent, "}");
    }

    private void genForwardDecl(int aInIndent, MClass aInClass)
    {
        String lNs = getNamespace(aInClass, false);

            out.println(aInIndent, "namespace " + lNs);
            out.println(aInIndent , "{");
                out.printHeaderComment(aInIndent + 1, "forward declaration for " + aInClass);
                out.println(aInIndent + 1, "class " + aInClass.getLID().getName() + ";");
            out.println(aInIndent, "} // namespace " + lNs);
    }

    private void genIncludes(int aInIndent, MClass aInClass)
    {
        out.println();
        out.println(aInIndent,getInclude("boost/optional.hpp", true));
        out.println(aInIndent,getInclude("opflex/modb/URIBuilder.h", false));
        out.println(aInIndent,getInclude("opflex/modb/mo-internal/MO.h", false));
        if (aInClass.hasSuperclass())
        {
            MClass lSuper = aInClass.getSuperclass();
            out.printIncodeComment(aInIndent, "superclass: " + lSuper);
            out.println(aInIndent,getInclude(lSuper), false);
        }
        TreeMap<Ident, MClass> lConts = new TreeMap<Ident, MClass>();
        aInClass.getContainsClasses(lConts, true, true);
        for (MClass lThis : lConts.values())
        {
            out.printIncodeComment(aInIndent, "contains: " + lThis);
            out.println(aInIndent,getInclude(lThis), false);
        }
    }

    private void genBody(int aInIndent, MClass aInClass)
    {
        out.println();
        String lNs = getNamespace(aInClass, false);

        out.println(aInIndent, "namespace opflex");
        out.println(aInIndent, "{");
            out.println(aInIndent + 1, "namespace " + lNs);
            out.println(aInIndent + 1, "{");
                genClass(aInIndent + 2, aInClass);
            out.println(aInIndent + 1, "} // namespace " + lNs);
        out.println(aInIndent, "} // namespace opflex");
    }


    private void genClass(int aInIndent, MClass aInClass)
    {
        out.println(aInIndent, "class " + aInClass.getLID().getName());
        if (aInClass.hasSuperclass())
        {
            MClass lSuperclass = aInClass.getSuperclass();
            out.println(aInIndent + 1, ": public " + getClassName(lSuperclass,true));
        }
        else
        {
            out.println(aInIndent + 1, ": public opflex::modb::mointernal::MO");
        }
        out.println(aInIndent, "{");
        out.println(aInIndent, "}; // class " + aInClass.getLID().getName());
    }
}

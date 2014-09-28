package genie.content.format.structure.cpp;

import genie.content.model.mclass.MClass;
import genie.content.model.mprop.MProp;
import genie.content.model.mtype.Language;
import genie.content.model.mtype.MType;
import genie.engine.file.WriteStats;
import genie.engine.format.*;
import genie.engine.model.Ident;
import genie.engine.model.Item;
import modlan.report.Severity;
import modlan.utils.Strings;

import java.util.Collection;
import java.util.LinkedList;
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
            out.println(aInIndent, getInclude(lThis), false);
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
            genPublic(aInIndent + 1, aInClass);
        out.println(aInIndent, "}; // class " + aInClass.getLID().getName());
    }

    private void genPublic(int aInIndent, MClass aInClass)
    {
        out.println(aInIndent, "public:");
        out.println();
        genClassId(aInIndent + 1, aInClass);
        genProps(aInIndent + 1, aInClass);
        genConstructor(aInIndent + 1, aInClass);
    }

    private void genClassId(int aInIndent, MClass aInClass)
    {
        out.println(aInIndent, "static const opflex::modb::class_id_t CLASS_ID = " + aInClass.getGID().getId() + ";");
        out.println();
    }

    private void genProps(int aInIndent, MClass aInClass)
    {
        TreeMap<String, MProp> lProps = new TreeMap<String, MProp>();
        aInClass.findProp(lProps, false);
        int lCnt=0;
        for (MProp lProp : lProps.values())
        {
            propNameToId.put(lProp.getLID().getName(),++lCnt);
            // ONLY IF THIS PROPERTY IS DEFINED LOCALLY
            if (lProp.getBase().getMClass() == aInClass)
            {
                genProp(aInIndent, aInClass, lProp, lCnt);
            }
        }
    }

    private void genProp(int aInIndent, MClass aInClass, MProp aInProp, int aInPropIdx)
    {
        MProp lBaseProp = aInProp.getBase();
        MType lType = lBaseProp.getType(false);
        MType lBaseType = lType.getBuiltInType();

        LinkedList<String> lComments = new LinkedList<String>();
        aInProp.getComments(lComments);


        genPropCheck(aInIndent,aInClass,aInProp,aInPropIdx,lType,lBaseType,lComments);
        genPropAccessor(aInIndent, aInClass, aInProp, aInPropIdx, lType, lBaseType, lComments);
        genPropDefaultedAccessor(aInIndent, aInClass, aInProp, aInPropIdx, lType, lBaseType, lComments);

    }

    private void genPropCheck(
            int aInIndent, MClass aInClass, MProp aInProp, int aInPropIdx, MType aInType, MType aInBaseType,
            Collection<String> aInComments)
    {
        //
        // COMMENT
        //
        int lCommentSize = 2 + aInComments.size();
        String lComment[] = new String[lCommentSize];
        int lCommentIdx = 0;
        lComment[lCommentIdx++] = "Check whether " + aInProp.getLID().getName() + " has been set";
        for (String lCommLine : aInComments)
        {
            lComment[lCommentIdx++] = lCommLine;
        }
        lComment[lCommentIdx++] = "@return true if " + aInProp.getLID().getName() + " has been set";
        out.printHeaderComment(aInIndent,lComment);
        //
        // METHOD DEFINITION
        //
        out.println(aInIndent,"boolean is" + Strings.upFirstLetter(aInProp.getLID().getName()) + "Set()");

        //
        // METHOD BODY
        //
        out.println(aInIndent,"{");
            out.println(aInIndent + 1, "return getObjectInstance().isSet(" + aInPropIdx +
                                       ", opflex::modb::PropertyInfo::" + aInBaseType.getLID().getName().toUpperCase() + ");");
        out.println(aInIndent,"}");
        out.println();
    }

    /**
    boost::optional<int64_t> getProp4()
    {
        if (isProp4Set())
            return getObjectInstance().getInt64(4);
         return boost::none;
    }
    **/
    private void genPropAccessor(
            int aInIndent, MClass aInClass, MProp aInProp, int aInPropIdx, MType aInType, MType aInBaseType,
            Collection<String> aInComments)
    {
        //
        // COMMENT
        //
        int lCommentSize = 2 + aInComments.size();
        String lComment[] = new String[lCommentSize];
        int lCommentIdx = 0;
        lComment[lCommentIdx++] = "Get the value of " + aInProp.getLID().getName() + " if it has been set.";

        for (String lCommLine : aInComments)
        {
            lComment[lCommentIdx++] = lCommLine;
        }
        lComment[lCommentIdx++] = "@return the value of " + aInProp.getLID().getName() + " or boost::none if not set";
        out.printHeaderComment(aInIndent,lComment);
        out.println(aInIndent,"boost::optional<" + aInBaseType.getLanguageBinding(Language.CPP).getSyntax() + "> get" + Strings.upFirstLetter(aInProp.getLID().getName()) + "()");
        out.println(aInIndent,"{");
            out.println(aInIndent + 1,"return is" + Strings.upFirstLetter(aInProp.getLID().getName()) + "Set() ?");
                out.println(aInIndent + 2,"getObjectInstance().get" + Strings.upFirstLetter(aInBaseType.getLID().getName()) + "(" + aInPropIdx + ") :");
                out.println(aInIndent + 2,"boost::none;");
        out.println(aInIndent,"}");
        out.println();
    }

    private void genPropDefaultedAccessor(
            int aInIndent, MClass aInClass, MProp aInProp, int aInPropIdx, MType aInType, MType aInBaseType,
            Collection<String> aInComments)
    {
        //
        // COMMENT
        //
        int lCommentSize = 3 + aInComments.size();
        String lComment[] = new String[lCommentSize];
        int lCommentIdx = 0;
        lComment[lCommentIdx++] = "Get the value of " + aInProp.getLID().getName() + " if set, otherwise the value of default passed in.";

        for (String lCommLine : aInComments)
        {
            lComment[lCommentIdx++] = lCommLine;
        }
        lComment[lCommentIdx++] = "@param defaultValue default value returned if the property is not set";
        lComment[lCommentIdx++] = "@return the value of " + aInProp.getLID().getName() + " if set, otherwise the value of default passed in";
        out.printHeaderComment(aInIndent,lComment);
        String lSyntax = aInBaseType.getLanguageBinding(Language.CPP).getSyntax();
        out.println(aInIndent, lSyntax + " get" + Strings.upFirstLetter(aInProp.getLID().getName()) + "(" + lSyntax + " defaultValue)");
        out.println(aInIndent,"{");
            out.println(aInIndent + 1, "return get" + Strings.upFirstLetter(aInProp.getLID().getName()) + "().get_value_or(defaultValue);");
        out.println(aInIndent,"}");
        out.println();
    }


    private void genConstructor(int aInIdent, MClass aInClass)
    {
        if (aInClass.hasSubclasses())
        {
            out.println(aInIdent, aInClass.getLID().getName() + "(");
            out.println(aInIdent + 1, "opflex::ofcore::OFFramework& framework,");
            out.println(aInIdent + 1, "opflex::modb::ClassId classId,");
            out.println(aInIdent + 1, "const opflex::modb::URI& uri,");
            out.println(aInIdent + 1, "const boost::shared_ptr<const opflex::modb::mointernal::ObjectInstance>& oi)");
            if (aInClass.hasSuperclass())
            {
                MClass lSuperclass = aInClass.getSuperclass();
                out.println(aInIdent + 2, ": " + getClassName(lSuperclass,true) + "(framework, classId, uri, oi) {}");
            }
            else
            {
                out.println(aInIdent + 2, ": MO(framework, classId, uri, oi) { }");
            }
        }
        if (aInClass.isConcrete())
        {
            out.println(aInIdent, aInClass.getLID().getName() + "(");
            out.println(aInIdent + 1, "opflex::ofcore::OFFramework& framework,");
            out.println(aInIdent + 1, "const opflex::modb::URI& uri,");
            out.println(aInIdent + 1, "const boost::shared_ptr<const opflex::modb::mointernal::ObjectInstance>& oi)");
            if (aInClass.hasSuperclass())
            {
                MClass lSuperclass = aInClass.getSuperclass();
                out.println(aInIdent + 2, ": " + getClassName(lSuperclass,true) + "(framework, CLASS_ID, uri, oi) {}");
            }
            else
            {
                out.println(aInIdent + 2, ": MO(framework, CLASS_ID, uri, oi) { }");
            }
        }
    }
    private TreeMap<String, Integer> propNameToId = new TreeMap<String, Integer>();
}

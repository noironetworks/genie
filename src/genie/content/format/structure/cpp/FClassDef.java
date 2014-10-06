package genie.content.format.structure.cpp;

import genie.content.format.meta.cpp.FMetaDef;
import genie.content.model.mclass.MClass;
import genie.content.model.mnaming.MNameComponent;
import genie.content.model.mnaming.MNameRule;
import genie.content.model.mnaming.MNamer;
import genie.content.model.module.Module;
import genie.content.model.mprop.MProp;
import genie.content.model.mtype.Language;
import genie.content.model.mtype.MLanguageBinding;
import genie.content.model.mtype.MType;
import genie.content.model.mtype.PassBy;
import genie.engine.file.WriteStats;
import genie.engine.format.*;
import genie.engine.model.Ident;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import modlan.report.Severity;
import modlan.utils.Strings;

import java.sql.Struct;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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

    public static boolean shouldTriggerTask(Item aIn)
    {
        return ((MClass)aIn).isConcrete();
    }

    public void generate()
    {
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
        /**
        if (aInClass.hasSuperclass())
        {
            MClass lSuper = aInClass.getSuperclass();
            out.printIncodeComment(aInIndent, "superclass: " + lSuper);
            out.println(aInIndent,getInclude(lSuper), false);
        }
         */
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
        /**
        if (aInClass.hasSuperclass())
        {
            MClass lSuperclass = aInClass.getSuperclass();
            out.println(aInIndent + 1, ": public " + getClassName(lSuperclass,true));
        }
        else**/
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
        genResolvers(aInIndent + 1, aInClass);
        genListenerReg(aInIndent + 1, aInClass);
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
        aInClass.findProp(lProps, true); // false
        for (MProp lProp : lProps.values())
        {
            // ONLY IF THIS PROPERTY IS DEFINED LOCALLY
            //if (lProp.getBase().getMClass() == aInClass)
            {
                genProp(aInIndent, aInClass, lProp, lProp.getPropId());
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
        genPropMutator(aInIndent, aInClass, aInProp, aInPropIdx, lType, lBaseType, lComments);
        genPropUnset(aInIndent, aInClass, aInProp, aInPropIdx, lType, lBaseType, lComments);
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
        //
        // DEF
        //
        lComment[lCommentIdx++] = "@param defaultValue default value returned if the property is not set";
        lComment[lCommentIdx++] = "@return the value of " + aInProp.getLID().getName() + " if set, otherwise the value of default passed in";
        out.printHeaderComment(aInIndent,lComment);
        String lSyntax = aInBaseType.getLanguageBinding(Language.CPP).getSyntax();
        out.println(aInIndent, lSyntax + " get" + Strings.upFirstLetter(aInProp.getLID().getName()) + "(" + lSyntax + " defaultValue)");
        //
        // BODY
        //
        out.println(aInIndent,"{");
            out.println(aInIndent + 1, "return get" + Strings.upFirstLetter(aInProp.getLID().getName()) + "().get_value_or(defaultValue);");
        out.println(aInIndent,"}");
        out.println();
    }

    private void genPropMutator(int aInIndent, MClass aInClass, MProp aInProp, int aInPropIdx, MType aInType, MType aInBaseType,
            Collection<String> aInComments)
    {
        //
        // COMMENT
        //
        int lCommentSize = 5 + aInComments.size();
        String lComment[] = new String[lCommentSize];
        int lCommentIdx = 0;
        lComment[lCommentIdx++] = "set " + aInProp.getLID().getName() + " to the specified value in the currently-active mutator.";

        for (String lCommLine : aInComments)
        {
            lComment[lCommentIdx++] = lCommLine;
        }
        lComment[lCommentIdx++] = "@param newValue the new value to set.";
        lComment[lCommentIdx++] = "@return a reference to the current object";
        lComment[lCommentIdx++] = "@throws std::logic_error if no mutator is active";
        lComment[lCommentIdx++] = "@see opflex::modb::Mutator";
        out.printHeaderComment(aInIndent,lComment);
        //
        // DEF
        //
        String lSyntax = aInBaseType.getLanguageBinding(Language.CPP).getSyntax();

        out.println(aInIndent,  getClassName(aInClass, true) + "& set" + Strings.upFirstLetter(aInProp.getLID().getName()) + "(" + lSyntax + " newValue)");
        //
        // BODY
        //
        out.println(aInIndent,"{");
        out.println(aInIndent + 1, "getTLMutator().modify(getClassId(), getURI())->set" + Strings.upFirstLetter(aInBaseType.getLID().getName()) + "(" + aInPropIdx + ", newValue);");
        out.println(aInIndent + 1, "return *this;");
        out.println(aInIndent,"}");
        out.println();
    }

    private void genPropUnset(int aInIndent, MClass aInClass, MProp aInProp, int aInPropIdx, MType aInType, MType aInBaseType,
            Collection<String> aInComments)
    {
        //
        // COMMENT
        //
        int lCommentSize = 4 + aInComments.size();
        String lComment[] = new String[lCommentSize];
        int lCommentIdx = 0;
        lComment[lCommentIdx++] = "unset " + aInProp.getLID().getName() + " in the currently-active mutator.";

        for (String lCommLine : aInComments)
        {
            lComment[lCommentIdx++] = lCommLine;
        }
        lComment[lCommentIdx++] = "@throws std::logic_error if no mutator is active";
        lComment[lCommentIdx++] = "@return a reference to the current object";
        lComment[lCommentIdx++] = "@see opflex::modb::Mutator";
        out.printHeaderComment(aInIndent,lComment);
        //
        // DEF
        //
        String lSyntax = aInBaseType.getLanguageBinding(Language.CPP).getSyntax();

        out.println(aInIndent,  getClassName(aInClass, true) + "& unset" + Strings.upFirstLetter(aInProp.getLID().getName()) + "()");
        //
        // BODY
        //
        out.println(aInIndent,"{");
        out.println(aInIndent + 1, "getTLMutator().modify(getClassId(), getURI())->unset(" + aInPropIdx + ", " +
                                   "opflex::modb::PropertyInfo::" + aInBaseType.getLID().getName().toUpperCase() + ", " +
                                   "opflex::modb::PropertyInfo::" +  aInBaseType.getTypeHint().getInfo().toString().toUpperCase() +
                                   ");");
        out.println(aInIndent + 1, "return *this;");
        out.println(aInIndent,"}");
        out.println();
    }

    private void genResolvers(int aInIdent, MClass aInClass)
    {
        if (aInClass.isRoot())
        {
            genRootCreation(aInIdent,aInClass);
        }
        if (aInClass.isConcrete())
        {
            genSelfResolvers(aInIdent, aInClass);
        }
        genChildrenResolvers(aInIdent, aInClass);
    }

    private void genRootCreation(int aInIdent, MClass aInClass)
    {
        String lClassName = getClassName(aInClass, true);
        out.println(aInIdent, "static boost::shared_ptr<" + lClassName + "> createRootElement(opflex::ofcore::OFFramework& framework)");
        out.println(aInIdent,"{");
            out.println(aInIdent + 1, "return opflex::modb::mointernal::MO::createRootElement<" + lClassName + ">(framework, CLASS_ID);");
        out.println(aInIdent, "}");
        out.println();
        out.println(aInIdent, "static boost::shared_ptr<" + lClassName + "> createRootElement()");
        out.println(aInIdent,"{");
        out.println(aInIdent + 1, "return createRootElement(opflex::ofcore::OFFramework::defaultInstance());;");
        out.println(aInIdent, "}");
        out.println();
    }

    private void genChildrenResolvers(int aInIdent, MClass aInClass)
    {
        TreeMap<Ident,MClass> lConts = new TreeMap<Ident, MClass>();
        aInClass.getContainsClasses(lConts, true, true);//true, true);
        for (MClass lChildClass : lConts.values())
        {
            genChildResolvers(aInIdent,aInClass,lChildClass);
        }
    }

    private void genSelfResolvers(int aInIdent, MClass aInClass)
    {
        String lFullyQualifiedClassName = getClassName(aInClass, true);
        out.println(aInIdent, "static boost::optional<boost::shared_ptr<" + lFullyQualifiedClassName + "> > resolve(");
        out.println(aInIdent + 1, "opflex::ofcore::OFFramework& framework,");
        out.println(aInIdent + 1, "const opflex::modb::URI& uri)");
        out.println(aInIdent, "{");
            out.println(aInIdent + 2, "return opflex::modb::mointernal::MO::resolve<" + lFullyQualifiedClassName + ">(framework, CLASS_ID, uri);");
        out.println(aInIdent, "}");
        out.println();
        out.println(aInIdent, "static boost::optional<boost::shared_ptr<" + lFullyQualifiedClassName + "> > resolve(");
        out.println(aInIdent + 1, "const opflex::modb::URI& uri)");
        out.println(aInIdent, "{");
        out.println(aInIdent + 2, "return opflex::modb::mointernal::MO::resolve<" + lFullyQualifiedClassName + ">(opflex::ofcore::OFFramework::defaultInstance(), CLASS_ID, uri);");
        out.println(aInIdent, "}");
        out.println();
        Collection<List<Pair<String, MNameRule>>> lNamingPaths = new LinkedList<List<Pair<String, MNameRule>>>();
        boolean lIsUniqueNaming = !aInClass.getNamingPaths(lNamingPaths, Language.CPP);
        for (List<Pair<String, MNameRule>> lNamingPath : lNamingPaths)
        {
            genNamedSelfResolvers(aInIdent, aInClass, lNamingPath, lIsUniqueNaming);
        }

        // TODO:
    }

    private static String getResolverMethName(List<Pair<String, MNameRule>> aInNamingPath, boolean aInIsUniqueNaming)
    {
        if (aInIsUniqueNaming)
        {
            return "resolve";
        }
        else
        {
            StringBuilder lSb = new StringBuilder();
            lSb.append("resolveUnder");
            int lSize = aInNamingPath.size();
            int lIdx = lSize;
            for (Pair<String, MNameRule> lPathNode : aInNamingPath)
            {
                if (0 < --lIdx)
                {
                    MClass lClass = MClass.get(lPathNode.getFirst());
                    Module lMod = lClass.getModule();
                    MNameRule lNr = lPathNode.getSecond();

                    lSb.append(Strings.upFirstLetter(lMod.getLID().getName()));
                    lSb.append(Strings.upFirstLetter(lClass.getLID().getName()));
                }
            }
            return lSb.toString();
        }
    }

    public static int countNamingProps(List<Pair<String, MNameRule>> aInNamingPath)
    {
        int lRet = 0;
        for (Pair<String, MNameRule> lNode : aInNamingPath)
        {
            Collection<MNameComponent> lNcs = lNode.getSecond().getComponents();
            if (!(null == lNcs || lNcs.isEmpty()))
            {
                for (MNameComponent lNc : lNcs)
                {
                    if (lNc.hasPropName())
                    {
                        lRet++;
                    }
                }
            }
        }
        return lRet;
    }

    public static void getPropParamName(MClass aInClass, String aInPropName, StringBuilder aOutSb)
    {
        aOutSb.append(aInClass.getModule().getLID().getName());
        aOutSb.append(Strings.upFirstLetter(aInClass.getLID().getName()));
        aOutSb.append(Strings.upFirstLetter(aInPropName));
    }

    public static String getPropParamName(MClass aInClass, String aInPropName)
    {
        StringBuilder lSb = new StringBuilder();
        getPropParamName(aInClass,aInPropName,lSb);
        return lSb.toString();
    }

    public static String getPropParamDef(MClass aInClass, String aInPropName)
    {
        MProp lProp = aInClass.findProp(aInPropName, false);
        if (null == lProp)
        {
            Severity.DEATH.report(aInClass.toString(),
                                  "preparing param defs for prop: " + aInPropName,
                                  "no such property: " + aInPropName, "");
        }
        MProp lBaseProp = lProp.getBase();
        MType lType = lBaseProp.getType(false);
        MType lBaseType = lType.getBuiltInType();
        MLanguageBinding lLang = lBaseType.getLanguageBinding(Language.CPP);
        String lSyntax = lLang.getSyntax();
        PassBy lPassBy = lLang.getPassBy();
        boolean lPassAsConst = lLang.getPassConst();
        StringBuilder lRet = new StringBuilder();
        if (lPassAsConst)
        {
            lRet.append("const ");
        }
        lRet.append(lSyntax);

        switch (lPassBy)
        {
            case REFERENCE:
            case POINTER:

                lRet.append('&');
                break;

            case VALUE:
            default:

                break;
        }
        lRet.append(" ");
        getPropParamName(aInClass, aInPropName, lRet);
        return lRet.toString();
    }

    public static String getUriBuilder(MClass aInClass, List<Pair<String, MNameRule>> aInNamingPath)
    {
        StringBuilder lSb = new StringBuilder();
        getUriBuilder(aInClass,aInNamingPath, lSb);
        return lSb.toString();
    }

    public static void getUriBuilder(MClass aInClass, List<Pair<String, MNameRule>> aInNamingPath, StringBuilder aOut)
    {
        aOut.append("opflex::modb::URIBuilder()");
        for (Pair<String,MNameRule> lNamingNode : aInNamingPath)
        {
            MNameRule lNr = lNamingNode.getSecond();
            MClass lThisContClass = MClass.get(lNamingNode.getFirst());
            Collection<MNameComponent> lNcs = lNr.getComponents();
            aOut.append(".addElement(\"");

            aOut.append(lThisContClass.getFullConcatenatedName());
            aOut.append("\")");
            for (MNameComponent lNc : lNcs)
            {
                if (lNc.hasPropName())
                {
                    aOut.append(".addElement(");
                    getPropParamName(lThisContClass, lNc.getPropName(), aOut);
                    aOut.append(")");
                }
            }
        }
        aOut.append(".build()");
    }

    public static String getUriBuilder(MClass aInParentClass,MClass aInChildClass, MNameRule aInNamingRule)
    {
        StringBuilder lSb = new StringBuilder();
        getUriBuilder(aInParentClass, aInChildClass, aInNamingRule, lSb);
        return lSb.toString();

    }
    public static void getUriBuilder(MClass aInParentClass,MClass aInChildClass, MNameRule aInNamingRule, StringBuilder aOut)
    {
        aOut.append("opflex::modb::URIBuilder(getURI())");
        aOut.append(".addElement(\"");
        aOut.append(aInChildClass.getFullConcatenatedName());
        aOut.append("\")");
        Collection<MNameComponent> lNcs = aInNamingRule.getComponents();
        for (MNameComponent lNc : lNcs)
        {
            if (lNc.hasPropName())
            {
                aOut.append(".addElement(");
                getPropParamName(aInChildClass, lNc.getPropName(), aOut);
                aOut.append(")");
            }
        }
        aOut.append(".build()");
    }

    public static String getNamingPropList(MClass aInClass, List<Pair<String, MNameRule>> aInNamingPath)
    {
        StringBuilder lSb = new StringBuilder();
        getNamingPropList(aInClass, aInNamingPath, lSb);
        return lSb.toString();
    }

    public static void getNamingPropList(MClass aInClass, List<Pair<String, MNameRule>> aInNamingPath, StringBuilder aOut)
    {
        boolean lIsFirst = true;
        for (Pair<String,MNameRule> lNamingNode : aInNamingPath)
        {
            MNameRule lNr = lNamingNode.getSecond();
            MClass lThisContClass = MClass.get(lNamingNode.getFirst());
            Collection<MNameComponent> lNcs = lNr.getComponents();

            for (MNameComponent lNc : lNcs)
            {
                if (lNc.hasPropName())
                {
                    if (lIsFirst)
                    {
                        lIsFirst = false;
                    }
                    else
                    {
                        aOut.append(',');
                    }
                    getPropParamName(lThisContClass, lNc.getPropName(), aOut);
                }
            }
        }
    }


    private void genNamedSelfResolvers(int aInIdent, MClass aInClass, List<Pair<String, MNameRule>> aInNamingPath, boolean aInIsUniqueNaming)
    {
        String lMethodName = getResolverMethName(aInNamingPath, aInIsUniqueNaming);
        //int lPropCount = countNamingProps(aInNamingPath);

        out.println(aInIdent,"static boost::::optional<boost::shared_ptr<" + getClassName(aInClass,true)+ "> > " + lMethodName + "(");
            out.print(aInIdent + 1, "opflex::ofcore::OFFramework& framework");
            for (Pair<String,MNameRule> lNamingNode : aInNamingPath)
            {
                MNameRule lNr = lNamingNode.getSecond();
                MClass lThisContClass = MClass.get(lNamingNode.getFirst());

                Collection<MNameComponent> lNcs = lNr.getComponents();
                for (MNameComponent lNc : lNcs)
                {
                    if (lNc.hasPropName())
                    {
                        out.println(",");
                        out.print(aInIdent + 1, getPropParamDef(lThisContClass, lNc.getPropName()));
                    }
                }
            }
            out.println(")");
        out.println(aInIdent,"{");
            out.println(aInIdent + 1, "resolve(framework," + getUriBuilder(aInClass, aInNamingPath) + ");");
        out.println(aInIdent,"}");
        out.println();
        out.println(aInIdent,
                    "static boost::::optional<boost::shared_ptr<" + getClassName(aInClass, true) + "> > " + lMethodName + "(");
        boolean lIsFirst = true;
        for (Pair<String,MNameRule> lNamingNode : aInNamingPath)
        {
            MNameRule lNr = lNamingNode.getSecond();
            MClass lThisContClass = MClass.get(lNamingNode.getFirst());

            Collection<MNameComponent> lNcs = lNr.getComponents();
            for (MNameComponent lNc : lNcs)
            {
                if (lNc.hasPropName())
                {
                    if (lIsFirst)
                    {
                        lIsFirst = false;
                    }
                    else
                    {
                        out.println(",");
                    }
                    out.print(aInIdent + 1, getPropParamDef(lThisContClass, lNc.getPropName()));
                }
            }
        }
        if (lIsFirst)
        {
            out.println(aInIdent + 1, ")");
        }
        else
        {
            out.println(")");
        }
        out.println(aInIdent,"{");
        out.println(aInIdent + 1, lMethodName + "(opflex::ofcore::OFFramework::defaultInstance()," + getNamingPropList(aInClass, aInNamingPath) + ");");
        out.println(aInIdent,"}");
        out.println();

    }

    private void genChildResolvers(int aInIdent, MClass aInParentClass, MClass aInChildClass)
    {
        MNamer lChildNamer = MNamer.get(aInChildClass.getGID().getName(),false);
        MNameRule lChildNr = lChildNamer.findNameRule(aInParentClass.getGID().getName());
        if (null != lChildNr)
        {
            String lFormattefChildClassName = getClassName(aInChildClass,true);
            String lConcatenatedChildClassName = aInChildClass.getFullConcatenatedName();
            String lUriBuilder = getUriBuilder(aInParentClass,aInChildClass, lChildNr);
            out.println(aInIdent, "boost::optional<boost::shared_ptr<" +  lFormattefChildClassName + "> > resolve" + lConcatenatedChildClassName + "(");

                boolean lIsFirst = true;
                Collection<MNameComponent> lNcs = lChildNr.getComponents();
                for (MNameComponent lNc : lNcs)
                {
                    if (lNc.hasPropName())
                    {
                        if (lIsFirst)
                        {
                            lIsFirst = false;
                        }
                        else
                        {
                            out.println(",");
                        }
                        out.print(aInIdent + 1, getPropParamDef(aInChildClass, lNc.getPropName()));
                    }
                }
                if (lIsFirst)
                {
                    out.println(aInIdent + 1, ")");
                }
                else
                {
                    out.println(")");
                }
            out.println(aInIdent,"{");
                out.println(aInIdent + 1, "return " + lFormattefChildClassName + "::resolve(getFramework(), " + lUriBuilder + ");");
            out.println(aInIdent,"}");
            out.println();
            out.println(aInIdent,"void resolve" + lConcatenatedChildClassName + "( /* out */ std::vector<boost::shared_ptr<" + lFormattefChildClassName+ "> >& out)");
            out.println(aInIdent,"{");
                out.println(aInIdent + 1, "return opflex::modb::mointernal::MO::resolveChildren<" + lFormattefChildClassName + ">(");
                    out.println(aInIdent + 2, "CLASS_ID, getURI()," + (aInChildClass.getGID().getId() + 1000) + "," + aInChildClass.getGID().getId() + ", out);");
            out.println(aInIdent,"}");
            out.println();

            out.println(aInIdent, "boost::optional<boost::shared_ptr<" +  lFormattefChildClassName + "> > add" + lConcatenatedChildClassName + "(");

            lIsFirst = true;
            for (MNameComponent lNc : lNcs)
            {
                if (lNc.hasPropName())
                {
                    if (lIsFirst)
                    {
                        lIsFirst = false;
                    }
                    else
                    {
                        out.println(",");
                    }
                    out.print(aInIdent + 1, getPropParamDef(aInChildClass, lNc.getPropName()));
                }
            }
            if (lIsFirst)
            {
                out.println(aInIdent + 1, ")");
            }
            else
            {
                out.println(")");
            }
            out.println(aInIdent,"{");
                out.println(aInIdent + 1, "boost::shared_ptr<" + lFormattefChildClassName + "> result = addChild<" + lFormattefChildClassName+ ">(");
                    out.println(aInIdent + 2, "CLASS_ID, getURI(), " + (aInChildClass.getGID().getId() + 1000) + ", " + aInChildClass.getGID().getId() + ",");
                    out.println(aInIdent + 2, lUriBuilder);
                    out.println(aInIdent + 2, ");");
                out.println(aInIdent + 1, "return result;");
            out.println(aInIdent,"}");
            out.println();
        }
        else
        {
            Severity.DEATH.report(aInParentClass.toString(), "child object resolver for " + aInChildClass.getGID().getName()," no naming rule", "");
        }
    }

    private void genListenerReg(int aInIndent, MClass aInClass)
    {
        if (aInClass.isConcrete())
        {
            out.println(aInIndent, "static void registerListener(");
            out.println(aInIndent + 1, "opflex::ofcore::OFFramework& framework,");
            out.println(aInIndent + 1, "opflex::modb::ObjectListener* listener)");
            out.println(aInIndent, "{");
            out.println(aInIndent + 1, "opflex::modb::mointernal::MO::registerListener(framework, listener, CLASS_ID);");
            out.println(aInIndent, "}");
            out.println();
            out.println(aInIndent, "static void registerListener(");
            out.println(aInIndent + 1, "opflex::modb::ObjectListener* listener)");
            out.println(aInIndent, "{");
            out.println(aInIndent + 1, "opflex::modb::mointernal::MO::registerListener(opflex::ofcore::OFFramework::defaultInstance(), listener);");
            out.println(aInIndent, "}");
            out.println();
            out.println(aInIndent, "static void unregisterListener(");
            out.println(aInIndent + 1, "opflex::ofcore::OFFramework& framework,");
            out.println(aInIndent + 1, "opflex::modb::ObjectListener* listener)");
            out.println(aInIndent, "{");
            out.println(aInIndent + 1, "opflex::modb::mointernal::MO::unregisterListener(framework, listener, CLASS_ID);");
            out.println(aInIndent, "}");
            out.println();
            out.println(aInIndent, "static void unregisterListener(");
            out.println(aInIndent + 1, "opflex::modb::ObjectListener* listener)");
            out.println(aInIndent, "{");
            out.println(aInIndent + 1, "opflex::modb::mointernal::MO::unregisterListener(opflex::ofcore::OFFramework::defaultInstance(), listener);");
            out.println(aInIndent, "}");
            out.println();
        }
    }

    private void genConstructor(int aInIdent, MClass aInClass)
    {

        if (aInClass.isConcrete())
        {
            out.println(aInIdent, aInClass.getLID().getName() + "(");
            out.println(aInIdent + 1, "opflex::ofcore::OFFramework& framework,");
            out.println(aInIdent + 1, "const opflex::modb::URI& uri,");
            out.println(aInIdent + 1, "const boost::shared_ptr<const opflex::modb::mointernal::ObjectInstance>& oi)");
            /**if (aInClass.hasSuperclass())
            {
                MClass lSuperclass = aInClass.getSuperclass();
                out.println(aInIdent + 2, ": " + getClassName(lSuperclass,true) + "(framework, getClassId(), uri, oi) {}");
            }
            else**/
            {
                out.println(aInIdent + 2, ": MO(framework, CLASS_ID, uri, oi) { }");
            }
        }
        else
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
        }
    }
}
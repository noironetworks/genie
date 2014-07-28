package genie.content.model.mtype;

import genie.engine.model.Cat;
import modlan.report.Severity;
import modlan.utils.Strings;

import javax.swing.*;

/**
 * Created by dvorkinista on 7/7/14.
 */
public class MLanguageBinding
        extends SubTypeItem
{
    public static final Cat MY_CAT = Cat.getCreate("type:language-binding");

    /**
     * Constructor
     * @param aInType parent type for which this binding is defined
     * @param aInLang target language for which the binding is defined
     * @param aInSyntax  target language syntax
     * @param aInObjectSyntax target language object syntax
     * @param aInPassBy  identifies whether value is passed by: value, reference, pointer
     * @param aInPassConst identifies whether this type should always be passed as constant
     * @param aInInclude required include file for this type
     */
    public MLanguageBinding(
            MType aInType,
            Language aInLang,
            String aInSyntax,
            String aInObjectSyntax,
            PassBy aInPassBy,
            boolean aInPassConst,
            String aInInclude)
    {
        super(MY_CAT, aInType, aInLang.getName());
        if (aInType.isDerived())
        {
            Severity.DEATH.report(
                    this.toString(),
                    "add language binding",
                    "derived types don't have language bindings",
                    "can't add " + aInType + " language binding to type: " + aInType);
        }
        lang = aInLang;
        syntax = aInSyntax;
        if (Strings.isEmpty(syntax))
        {
            Severity.DEATH.report(
                    this.toString(),
                    "add language binding",
                    "",
                    "syntax not specified");
        }
        objectSyntax = null == aInObjectSyntax ? syntax : aInObjectSyntax;
        passBy = null == aInPassBy ? PassBy.REFERENCE : aInPassBy;
        passConst = aInPassConst || (passBy == PassBy.REFERENCE);
        include = aInInclude;
    }

    /**
     * Language accessor. Retrieves language with which this type binding is associated with
     * @return language with which this type binding is associated with
     */
    public Language getLanguage()
    {
        return lang;
    }

    /**
     * Syntax accessor. Retrieves target language syntax (like "uint32_t" in C++)
     * @return target language syntax
     */
    public String getSyntax()
    {
        return syntax;
    }

    /**
     * object syntax accessor: retrieves target language syntax for object containing this type...
     * for example, in java, a primitive int has object equivalent of java.lang.Integer
     * @return target language syntax for object containing this type
     */
    public String getObjectSyntax()
    {
        return objectSyntax;
    }

    /**
     * identifies whether value is passed by: value, reference, pointer
     * @return type of parameter passing
     */
    public PassBy getPassBy()
    {
        return passBy;
    }

    /**
     * identifies if the value should be passed as constant
     * @return type should always be passed as constant (true by default)
     */
    public boolean getPassConst()
    {
        return passConst;
    }

    /**
     * constraints accessor.
     * @return constrainst associated with this binding
     */
    public MConstraints getConstraints()
    {
        return (MConstraints) getChildItem(MConstraints.MY_CAT, MConstraints.NAME);
    }

    public MConstants getConstants()
    {
        return (MConstants) getChildItem(MConstants.MY_CAT, MConstants.NAME);
    }

    public void validateCb()
    {
        super.validateCb();
        MConstraints lConstr = getConstraints();
        if (null == lConstr)
        {
            Severity.DEATH.report(toString(),"validation","","primitive types must have defined constraints");
        }
        MConstants lConsts = getConstants();
        if (null == lConsts)
        {
            Severity.DEATH.report(toString(),"validation","","primitive types must have defined constants");
        }
        MType lIndirType = lConstr.getType();
        if (null == lIndirType)
        {
            Severity.DEATH.report(toString(),"validation","","constraints type is unresolvable");
        }
    }

    /**
     * language with which this type binding is associated with
     */
    private final Language lang;

    /**
     * target language syntax for this type.
     # like "uint32_t"
     */
    private final String syntax;

    /**
     * target language syntax for object containing this type...
     * for example, in java, a primitive int has object equivalent of java.lang.Integer
     */
    private final String objectSyntax;

    /**
     * identifies whether value is passed by: value, reference, pointer
     */
    private final PassBy passBy;

    /**
     * identifies whether this type should always be passed as constant (true by default)
     */
    private final boolean passConst;

    /**
     * required include file for this type
     */
    private final String include;
}
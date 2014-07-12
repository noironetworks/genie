package genie.content.model.mtype;

import genie.engine.model.Cat;
import modlan.report.Severity;

/**
 * Created by dvorkinista on 7/7/14.
 */
public class LanguageBinding extends SubTypeItem
{
    public static final Cat MY_CAT = Cat.getCreate("type:language-binding");

    public LanguageBinding(
            MType aInType, Language aInLang)
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
    }

}

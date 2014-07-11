package genie.content.model.mnaming;

import genie.content.model.mclass.MClass;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import modlan.report.Severity;

/**
 * Created by midvorki on 7/10/14.
 *
 * Specification of the naming rule. Identifies the naming properties used in formation of object LRIs/URIs.
 */
public class MNamer extends Item
{
    /**
     * category identifying all naming rules
     */
    public static final Cat MY_CAT = Cat.getCreate("mnamer");

    public MNamer(String aInGClassName)
    {
        super(MY_CAT,null,aInGClassName);
    }

    public MClass getTargetClass()
    {
        MClass lRet = MClass.get(getLID().getName());
        if (null == lRet)
        {
            Severity.DEATH.report(
                    this.toString(),
                    "retrieval of target class",
                    "class not found",
                    "naming rule can't find associated class.");
        }
        return lRet;
    }
}

package genie.content.model.mnaming;

import genie.content.model.mclass.MClass;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import modlan.report.Severity;

/**
 * Created by midvorki on 7/10/14.
 *
 * Specification of the namer rule for a class item. Identifies the naming properties used in
 * formation of object LRIs/URIs. For a given class, Namer is a model item that identifies rules
 * of how such class is named in a context of its many parents. Namer is identified by the global
 * name of the class to which it corresponds. Naming rules within a namer can be asymmetrical/distinct
 * amongst different containers. This is important, as managed object can have different naming properties based
 * on what context its instantiated under.
 */
public class MNamer extends Item
{
    /**
     * category identifying all namer rules
     */
    public static final Cat MY_CAT = Cat.getCreate("mnamer");

    /**
     * Constructor.
     * @param aInGClassName global class name corresponding to the class for which this naming rule is created.
     */
    public MNamer(String aInGClassName)
    {
        super(MY_CAT,null,aInGClassName);
    }

    /**
     * target class accessor. retrieves the class item corresponding to this naming rule.
     * @return class for which this naming rule exists
     */
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

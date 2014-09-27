package genie.content.model.mnaming;

import genie.content.model.mclass.MClass;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import genie.engine.model.Node;
import modlan.report.Severity;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

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

    public static synchronized MNamer get(String aInGClassName, boolean aInCreateIfNotFound)
    {
        MNamer lNamer = (MNamer) MY_CAT.getItem(aInGClassName);
        if (null == lNamer && aInCreateIfNotFound)
        {
            lNamer = new MNamer(aInGClassName);
        }
        return lNamer;
    }

    /**
     * Constructor.
     * @param aInGClassName global class name corresponding to the class for which this naming rule is created.
     */
    private MNamer(String aInGClassName)
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

    public synchronized MNameRule getNameRule(String aInClassOrAny, boolean aInCreateIfNotFound)
    {
        MNameRule lNr = (MNameRule) getChildItem(MNameRule.MY_CAT,aInClassOrAny);
        if (null == lNr && aInCreateIfNotFound)
        {
            lNr = new MNameRule(this,aInClassOrAny);
        }
        return lNr;
    }

    public void getNamingRules(Map<String, MNameRule> aOut)
    {
        LinkedList<Item> lNRs = new LinkedList<Item>();
        getChildItems(MNameRule.MY_CAT, lNRs);

        for (Item lIt : lNRs)
        {
            if (!aOut.containsKey(lIt.getGID().getName()))
            {
                aOut.put(lIt.getGID().getName(), (MNameRule) lIt);
            }
        }
    }
}

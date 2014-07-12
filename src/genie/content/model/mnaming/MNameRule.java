package genie.content.model.mnaming;

import genie.content.model.mclass.MClass;
import genie.engine.model.Cat;
import genie.engine.model.Ident;
import genie.engine.model.Item;
import modlan.report.Severity;
import modlan.utils.Strings;

import java.util.Collection;
import java.util.Map;

/**
 * Created by midvorki on 7/10/14.
 *
 * Name rule is a containment specific name rule, a model item that identifies: a) how an object
 * is named in a context of one parent b) how an object is named for the rest of the containment
 * context not explicitly specified
 */
public class MNameRule extends Item
{
    /**
     * category identifying all containment specific name rules
     */
    public static final Cat MY_CAT = Cat.getCreate("mnamer:rule");

    /**
     * Constructor
     * @param aInNamer a namer rule under which this containment specific rule is created
     * @param aInContainerClassOrAny specifies specific container class or "any"/null, in case general rule is desired.
     */
    public MNameRule(MNamer aInNamer, String aInContainerClassOrAny)
    {
        super(MY_CAT,
              aInNamer,
              Strings.isAny(aInContainerClassOrAny) ?
                NameRuleScope.ANY.getName() :
                aInContainerClassOrAny
                );
        isAnyTarget = Strings.isAny(aInContainerClassOrAny);
    }

    /**
     * namer rule accessor
     * @return namer rule that contains this item
     */
    public MNamer getNamer()
    {
        return (MNamer) getParent();
    }

    public MClass getTargetClassExplicit()
    {
        MClass lClass = MClass.get(getLID().getName());
        if (null == lClass)
        {
            Severity.DEATH.report(
                    this.toString(),
                    "retrieval of target class",
                    "class not found",
                    "naming rule can't find associated class.");
        }
        return lClass;
    }

    /**
     * target class(s) resolver.
     * @param aOut map of target classes
     */
    public void getTargetClass(Map<Ident,MClass> aOut)
    {
        MClass lContainerClass = null;
        if (isAnyTarget)
        {
            MNamer lNamer = getNamer();
            MClass lNamedClass = lNamer.getTargetClass();

            // resolved all of the
            lNamedClass.getContainedByClasses(aOut, true, true);
        }
        else
        {
            lContainerClass = getTargetClassExplicit();
            if (lContainerClass.isConcrete())
            {
                aOut.put(lContainerClass.getGID(),lContainerClass);
            }
            else
            {
                lContainerClass.getSubclasses(aOut, false, true);
            }

        }
    }

    private final boolean isAnyTarget;
}

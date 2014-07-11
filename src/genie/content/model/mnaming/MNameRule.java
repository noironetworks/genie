package genie.content.model.mnaming;

import genie.content.model.mclass.MClass;
import genie.engine.model.Cat;
import genie.engine.model.Ident;
import genie.engine.model.Item;
import modlan.report.Severity;
import modlan.utils.Strings;

import java.util.Map;

/**
 * Created by midvorki on 7/10/14.
 */
public class MNameRule extends Item
{
    public static final Cat MY_CAT = Cat.getCreate("mnamer:rule");

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

    // TODO:
    public void getTargetClass(Map<Ident,MClass> aOut)
    {
        MClass lClass = null;
        if (isAnyTarget)
        {
            MNamer lNamer = getNamer();
        }
        else
        {
            lClass = getTargetClassExplicit();
            aOut.put(lClass.getLID(), lClass);
        }
    }

    private final boolean isAnyTarget;
}

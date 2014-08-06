package genie.content.model.mrelator;

import genie.content.model.mclass.MClass;
import genie.engine.model.Cardinality;
import genie.engine.model.Cat;
import genie.engine.model.RelatorCat;

import java.util.Collection;

/**
 * Created by midvorki on 8/5/14.
 */
public class MSource extends MRelatorRuleItem
{
    public static final Cat MY_CAT = Cat.getCreate("rel:source");
    public static final RelatorCat TARGET_CAT = RelatorCat.getCreate("rel:source:target", Cardinality.SINGLE);

    public MSource(
            MRelated aInTarget,
            String aInChildGname)
    {
        super(MY_CAT, aInTarget, TARGET_CAT, aInChildGname);
    }

    public MRelated getRelated()
    {
        return (MRelated) getParent();
    }

    public MClass getRelatedClass()
    {
        return getRelated().getTarget();
    }

    public MRelator getMRelator()
    {
        return MRelator.get(getLID().getName());
    }

    public MTarget getMTarget()
    {
        return getMRelator().getMTarget(getRelated().getLID().getName());
    }

    public MRelationship getRelationship(String aInName)
    {
        return getMTarget().getRelationship(aInName);
    }

    public void getRelationships(Collection<MRelationship> aOut)
    {
        getMTarget().getRelationships(aOut);
    }

    public void validateCb()
    {
        super.validateCb();
        /**Severity.WARN.report(toString(),"validate", "",
         "\n\trelated: " + getRelated() + "\n" +
         "\trelated class: " + getRelatedClass() + "\n" +
         "\trelator: " + getMRelator() + "\n" +
         "\tmtarget: " + getMTarget());**/
    }

}
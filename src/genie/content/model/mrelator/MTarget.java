package genie.content.model.mrelator;

import genie.content.model.mclass.MClass;

import genie.engine.model.Cardinality;
import genie.engine.model.Cat;
import genie.engine.model.RelatorCat;

/**
 * Created by midvorki on 8/5/14.
 */
public class MTarget extends MRelatorRuleItem
{
    public static final Cat MY_CAT = Cat.getCreate("rel:target");
    public static final RelatorCat TARGET_CAT = RelatorCat.getCreate("rel:target:target", Cardinality.SINGLE);

    MTarget(
            MRelator aInSource,
            String aInTargetGname)
    {
        super(MY_CAT, aInSource, TARGET_CAT, aInTargetGname);
    }

    public MRelator getRelator()
    {
        return (MRelator) getParent();
    }

    public MClass getRelatorClass()
    {
        return getRelator().getTarget();
    }

    public MRelated getMRelated()
    {
        return MRelated.get(getLID().getName());
    }

    public MSource getMSource()
    {
        return getMRelated().getMSource(getRelator().getLID().getName());
    }

    public void validateCb()
    {
        super.validateCb();

        /*Severity.WARN.report(toString(),"validate", "",
                             "\n\trelator: " + getRelator() + "\n" +
                             "\trelator class: " + getRelatorClass() + "\n" +
                             "\trelated: " + getMRelated() + "\n" +
                             "\tmsource: " + getMSource());*/
    }
}

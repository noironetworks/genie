package genie.content.model.mrelator;

import genie.content.model.mclass.MClass;
import genie.engine.model.Cat;
import genie.engine.model.Item;

/**
 * Created by midvorki on 8/6/14.
 */
public class MRelationship extends Item
{
    public static final Cat MY_CAT = Cat.getCreate("rel:relationship");

    public MRelationship(
            MTarget aInParent,
            String aInName,
            RelatorType aInType,
            PointCardinality aInSourceCard,
            PointCardinality aInTargetCard)
    {
        super(MY_CAT,aInParent,aInName);
        type = aInType;
        sourceCardinality = aInSourceCard;
        targetCardinality = aInTargetCard;
    }

    public MTarget getMTarget()
    {
        return (MTarget) getParent();
    }

    public MSource getMSource()
    {
        return getMTarget().getMSource();
    }

    public MRelator getMRelator()
    {
        return getMTarget().getRelator();
    }

    public MRelated getMRelated()
    {
        return getMTarget().getMRelated();
    }

    public MClass getSourceClass()
    {
        return getMRelator().getTarget();
    }

    public String getSourceClassGName() { return getMRelator().getTargetGName(); }

    public MClass getTargetClass()
    {
        return getMTarget().getTarget();
    }

    public String getTargetClassGName()
    {
        return getMTarget().getTargetGName();
    }

    public RelatorType getType()
    {
        return type;
    }

    public PointCardinality getSourceCardinality() { return sourceCardinality; }
    public PointCardinality getTargetCardinality() { return targetCardinality; }

    private final RelatorType type;
    private final PointCardinality sourceCardinality;
    private final PointCardinality targetCardinality;
}

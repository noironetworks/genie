package genie.content.model.mrelator;

import genie.content.model.mclass.MClass;
import genie.content.model.mcont.MContained;
import genie.content.model.module.Module;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import modlan.utils.Strings;

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
        sourceRelnClass = initSourceRelnClass();
        targetRelnClass = initTargetRelnClass();
        resolverRelnClass = initResolverRelnClass();

        String lSrcClassGName = getSourceClassGName();
        int lSlashIdx = lSrcClassGName.indexOf('/');
        moduleName = lSrcClassGName.substring(0, lSlashIdx);
        sourceClassLocalName = lSrcClassGName.substring(0, lSrcClassGName.length());
    }


    public String getSourceClassGName() { return getMRelator().getTargetGName(); }
    public MClass getSourceClass() { return getMRelator().getTarget(); }

    public String getTargetClassGName() { return getMTarget().getTargetGName(); }
    public MClass getTargetClass(){ return getMTarget().getTarget(); }

    public boolean hasSourceRelnClass() { return type.hasSourceObject(); }
    public MClass getSourceRelnClass() { return sourceRelnClass; }
    public boolean hasTargetRelnClass() { return type.hasTargetObject(); }
    public MClass getTargetRelnClass() { return targetRelnClass; }
    public boolean hasResolverRelnClass() { return type.hasResolverObject(); }
    public MClass getResolverRelnClass() { return resolverRelnClass; }

    public MSource getMSource() { return getMTarget().getMSource(); }
    public MTarget getMTarget() { return (MTarget) getParent(); }
    public MRelator getMRelator() { return getMTarget().getRelator(); }
    public MRelated getMRelated() { return getMTarget().getMRelated(); }

    public RelatorType getType() { return type; }
    public PointCardinality getSourceCardinality() { return sourceCardinality; }
    public PointCardinality getTargetCardinality() { return targetCardinality; }

    /*
        source: module/ReSrc<LocalClassName><Name>
        target: module/ReTgt<LocalClassName><Name>
        resolver: module/ReRes<LocalClassName><Name>
     */

    private MClass initSourceRelnClass()
    {
        MClass lClass = null;
        if (type.hasSourceObject())
        {
            // CLASS NAME FORMAT: module/ReSrc<LocalClassName><Name>
            String lClassName = "ReSrc" + sourceClassLocalName + Strings.upFirstLetter(getLID().getName());
            Module lModule = Module.get(moduleName, true);
            lClass = new MClass(lModule, lClassName, true);
            lClass.addSuperclass(type.isNamed() ? "relator/NameResolvedRelSource" : "relator/DirectRelSource");

            MContained.addRule(getSourceClassGName(), lClass.getGID().getName());

            // TODO: PROPERTIES
            // TODO: ADD NAMING
        }
        return lClass;
    }

    private MClass initTargetRelnClass()
    {
        MClass lClass = null;
        if (type.hasTargetObject())
        {
            // CLASS NAME FORMAT: module/ReTgt<LocalClassName><Name>
            String lClassName = "ReTgt" + sourceClassLocalName + Strings.upFirstLetter(getLID().getName());
            Module lModule = Module.get(moduleName, true);
            lClass = new MClass(lModule, lClassName, true);
            lClass.addSuperclass("relator/Target");

            MContained.addRule(getTargetClassGName(), lClass.getGID().getName());

            // TODO: PROPERTIES
            // TODO: ADD NAMING
        }
        return lClass;
    }

    private MClass initResolverRelnClass()
    {
        MClass lClass = null;
        if (type.hasTargetObject())
        {
            // CLASS NAME FORMAT: module/ReRes<LocalClassName><Name>
            String lClassName = "ReTgt" + sourceClassLocalName + Strings.upFirstLetter(getLID().getName());
            Module lModule = Module.get(moduleName, true);
            lClass = new MClass(lModule, lClassName, true);
            lClass.addSuperclass("relator/Resolver");

            // TODO: WHAT SHOULD IT BE PARENTED BY? MContained.addRule(getSourceClassGName(), lClass.getGID().getName());

            // TODO: PROPERTIES
            // TODO: ADD NAMING
        }
        return lClass;
    }

    private final RelatorType type;
    private final PointCardinality sourceCardinality;
    private final PointCardinality targetCardinality;
    private final MClass sourceRelnClass;
    private final MClass targetRelnClass;
    private final MClass resolverRelnClass;
    private final String moduleName;
    private final String sourceClassLocalName;
}

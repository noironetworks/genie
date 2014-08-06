package genie.content.model.mrelator;

import modlan.report.Severity;

/**
 * Created by midvorki on 8/5/14.
 */
public enum RelatorType
{
    DIRECT_ASSOCIATION(false, true, true, false),
    NAMED_ASSOCIATION(true, true, true, false),
    DIRECT_DEPENDENCY(false, true, true, true),
    NAMED_DEPENDENCY(true, true, true, true),
    REFERENCE(false, true, false, false);

    public static RelatorType get(String aIn)
    {
        for (RelatorType lThis : RelatorType.values())
        {
            if (lThis.toString().equalsIgnoreCase(aIn))
            {
                return lThis;
            }
        }
        Severity.DEATH.report("relator-type", "get", "", "no relator type: " + aIn);

        return NAMED_DEPENDENCY;
    }
    private RelatorType(
            boolean aInIsNamed,
            boolean aInHasSourceObject,
            boolean aInHasTargetObject,
            boolean aInHasResolverObject)
    {
        isNamed = aInIsNamed;
        hasSourceObject = aInHasSourceObject;
        hasTargetObject = aInHasTargetObject;
        hasResolverObject = aInHasResolverObject;
    }

    public boolean isNamed() { return isNamed; }
    public boolean isDirect() { return !isNamed; }

    public boolean hasSourceObject() { return hasSourceObject; }
    public boolean hasTargetObject() { return hasTargetObject; }
    public boolean hasResolverObject() { return hasResolverObject; }

    private final boolean hasSourceObject;
    private final boolean hasTargetObject;
    private final boolean hasResolverObject;
    private final boolean isNamed;
}

package genie.content.model.mrelator;

import modlan.report.Severity;

/**
 * Created by midvorki on 8/5/14.
 */
public enum RelatorType
{
    DIRECT_ASSOCIATION,
    NAMED_ASSOCIATION,
    DIRECT_DEPENDENCY,
    NAMED_DEPENDENCY,
    REFERENCE;
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
}

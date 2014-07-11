package genie.content.model.mnaming;

import modlan.report.Severity;

/**
 * Created by midvorki on 7/10/14.
 */
public enum NameRuleScope
{
    SPECIFIC_CLASS("class"),
    ANY("any")
    ;
    private NameRuleScope(String aIn)
    {
        name = aIn;
    }

    public String getName()
    {
        return name;
    }

    public String toString()
    {
        return name;
    }

    public static NameRuleScope get(String aIn)
    {
        for (NameRuleScope lNRS : NameRuleScope.values())
        {
            if (aIn.equalsIgnoreCase(lNRS.getName()))
            {
                return lNRS;
            }
        }
        Severity.DEATH.report(
                "NameRuleScope",
                "get name rule scope",
                "no such name rule scope",
                "no support for " + aIn + "; name rule scopes supported: " + NameRuleScope.values());

        return null;
    }
    private final String name;

}

package genie.content.model.mvalidation;

import modlan.report.Severity;

/**
 * Created by midvorki on 7/10/14.
 */
public enum ValidatorAction
{
    ADD("add"),
    CLOBBER("clobber"),
    REMOVE("remove"),
    ;

    private ValidatorAction(String aIn)
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

    public static ValidatorAction get(String aIn)
    {
        for (ValidatorAction lVA : ValidatorAction.values())
        {
            if (aIn.equalsIgnoreCase(lVA.getName()))
            {
                return lVA;
            }
        }
        Severity.DEATH.report(
                "ValidatorAction",
                "get validator action for name",
                "no such validator action",
                "no support for " + aIn + "; actions supported: " + ValidatorAction.values());

        return null;
    }
    private final String name;
}

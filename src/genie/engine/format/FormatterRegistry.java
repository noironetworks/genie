package genie.engine.format;

import modlan.report.Severity;

import java.util.TreeMap;

/**
 * Created by midvorki on 7/24/14.
 */
public class FormatterRegistry
{
    public void addDomain(FormatterDomainMeta aIn)
    {
        if (!domains.containsKey(aIn.getName()))
        {
            domains.put(aIn.getName(),aIn);
        }
        else
        {
            Severity.DEATH.report(toString(),"","", "domain already registered: " + aIn.getName());
        }
    }

    public void process(FormatterCtx aInCtx)
    {
        for (FormatterDomainMeta lDom : domains.values())
        {
            if (lDom.isEnabled())
            {
                lDom.process(aInCtx);
            }
        }
    }
    public String toString()
    {
        return "formatter:registry";
    }
    private TreeMap<String,FormatterDomainMeta> domains = new TreeMap<String,FormatterDomainMeta>();
}

package genie.engine.format;

import modlan.report.Severity;

import java.util.TreeMap;

/**
 * Created by midvorki on 7/24/14.
 */
public class FormatterDomainMeta
{
    public FormatterDomainMeta(String aInName)
    {
        name = aInName;
    }

    public String getName()
    {
        return name;
    }

    public boolean isEnabled()
    {
        return isEnabled;
    }

    public void setEnabled(boolean aIn)
    {
        isEnabled = aIn;
    }

    public void process(FormatterCtx aInCtx)
    {
        //System.out.println(this + ".ptocess(" + aInCtx + ")");
        for (FormatterFeatureMeta lTask : tasks.values())
        {
            if (lTask.isEnabled())
            {
                //System.out.println(this + ".ptocess(" + aInCtx + "): " + lTask);

                lTask.process(aInCtx);
            }
        }
    }

    public void addFeature(FormatterFeatureMeta aIn)
    {
        if (!tasks.containsKey(aIn.getName()))
        {
            tasks.put(aIn.getName(),aIn);
        }
        else
        {
            Severity.DEATH.report(toString(),"","", "task already registered: " + aIn.getName());
        }
    }

    public String toString()
    {
        return "formatter:domain(" + name + ')';
    }

    private final String name;
    private boolean isEnabled = true;
    private TreeMap<String,FormatterFeatureMeta> tasks = new TreeMap<String,FormatterFeatureMeta>();
}

package genie.engine.format;

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

    public void process()
    {
        // TODO:
    }

    private final String name;
    private boolean isEnabled = true;
}

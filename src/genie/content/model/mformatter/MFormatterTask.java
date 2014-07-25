package genie.content.model.mformatter;

import genie.engine.model.Cat;
import genie.engine.model.Item;

/**
 * Created by midvorki on 7/24/14.
 */
public class MFormatterTask
        extends Item
{
    /**
     * category of this item
     */
    public static final Cat MY_CAT = Cat.getCreate("formatter:task");

    /**
     * Constructor
     * @param aInLName name of the feature
     */
    public MFormatterTask(MFormatterFeature aInDomain, String aInLName)
    {
        super(MY_CAT, aInDomain, aInLName);
    }

    public MFormatterFeature getFeature()
    {
        return (MFormatterFeature) getParent();
    }

    public MFormatterDomain getDomain()
    {
        return getFeature().getDomain();
    }
}

package genie.content.model.mformatter;

import genie.engine.model.Cardinality;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import genie.engine.model.RelatorCat;

/**
 * Created by midvorki on 7/24/14.
 */
public class MFormatterDomain
        extends Item
{
    /**
     * category of this item
     */
    public static final Cat MY_CAT = Cat.getCreate("formatter:domain");


    /**
     * Constructor
     * @param aInLName name of the domain
     */
    public MFormatterDomain(String aInLName)
    {
        super(MY_CAT, null, aInLName);
    }

    {

    }
}

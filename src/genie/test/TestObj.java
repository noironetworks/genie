package genie.test;

import genie.engine.model.Cat;
import genie.engine.model.Item;

/**
 * Created by midvorki on 3/27/14.
 */
public class TestObj extends Item
{
    public static final Cat CATEGORY = Cat.getCreate("test");
    public TestObj(Item aInParent, String aInLName)
    {
        super(CATEGORY, aInParent, aInLName);
    }
}

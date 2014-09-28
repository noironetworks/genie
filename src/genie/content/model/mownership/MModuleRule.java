package genie.content.model.mownership;

import genie.content.model.mclass.MClass;
import genie.engine.model.Cat;
import genie.engine.model.Item;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by midvorki on 9/27/14.
 */
public class MModuleRule extends MOwnershipRule
{
    public static final Cat MY_CAT = Cat.getCreate("mowner:module");
    public MModuleRule(MOwner aInParent, String aInNameOrAll)
    {
        super(MY_CAT,aInParent,aInNameOrAll,DefinitionScope.OWNER);
    }

    public void getClasses(Map<String, MClass> aOut)
    {
        LinkedList<Item> lIts = new LinkedList<Item>();
        getChildItems(MClassRule.MY_CAT, lIts);
        for (Item lIt : lIts)
        {
            ((MClassRule)lIt).getClasses(aOut);
        }
    }
}

package genie.content.model.mclass;

import genie.content.model.module.SubModuleItem;
import genie.engine.model.Cat;

/**
 * Created by dvorkinista on 7/7/14.
 */
public class SubStructItem extends SubModuleItem
{
    protected SubStructItem(Cat aInCat,MClass aInParent, String aInLName)
    {
        super(aInCat, aInParent, aInLName);
    }

    public MClass getMClass()
    {
        return MClass.getMClass(this);
    }
}
package genie.content.model.mprop;

import genie.content.model.mclass.MClass;
import genie.content.model.mclass.SubStructItem;
import genie.engine.model.Cat;

import java.util.Map;

/**
 * Created by midvorki on 8/16/14.
 */
public class MPropGroup extends SubStructItem
{
    public static final Cat MY_CAT = Cat.getCreate("mprop:group");

    public MPropGroup(MClass aInClass, String aInName)
    {
        super(MY_CAT,aInClass,aInName);
    }

    public MClass getMClass()
    {
        return (MClass) getParent();
    }

    public void getProps(Map<String,MProp> aOutProps)
    {
        getProps(getMClass(),aOutProps);
    }

    public void getProps(MClass aInClass, Map<String,MProp> aOutProps)
    {
        aInClass.findProp(aOutProps, getLID().getName());
    }
}

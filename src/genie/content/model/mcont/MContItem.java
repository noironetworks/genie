package genie.content.model.mcont;

import genie.content.model.mclass.MClass;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import genie.engine.model.RelatorCat;

/**
 * Created by dvorkinista on 7/8/14.
 */
abstract public class MContItem
        extends Item
{
    protected MContItem(
            Cat aInMyCat, Item aInParent, RelatorCat aInTargetRelatorCat, String aInTargetGname)
    {
        super(aInMyCat, aInParent, aInTargetGname);
        targetRelatorCat = aInTargetRelatorCat;
        addTargetRef(aInTargetGname);
    }

    /**
     * Retrieves target Class.
     * @return
     */
    public MClass getTarget()
    {
        return (MClass) getTargetRelatorCat().getRelator(getGID().getName()).getToItem();
    }

    /**
     * registers super type for this type. super type is the type from which this type is derived
     * @param aInTargetGName super type global name
     */
    private void addTargetRef(String aInTargetGName)
    {
        getTargetRelatorCat().add(getCat(), getGID().getName(), MClass.MY_CAT, aInTargetGName);
    }

    protected RelatorCat getTargetRelatorCat()
    {
        return targetRelatorCat;
    }

    private final RelatorCat targetRelatorCat;
}

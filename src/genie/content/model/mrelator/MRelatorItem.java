package genie.content.model.mrelator;

import genie.content.model.mclass.MClass;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import genie.engine.model.RelatorCat;

/**
 * Created by midvorki on 8/5/14.
 */
public class MRelatorItem
        extends Item
{
    /**
     * constructor
     * @param aInMyCat category of a containment item
     * @param aInParent parent item under which this item is created
     * @param aInTargetRelatorCat target class resolution category
     * @param aInTargetGname global name of the target class
     */
    protected MRelatorItem(
            Cat aInMyCat, Item aInParent, RelatorCat aInTargetRelatorCat, String aInTargetGname)
    {
        super(aInMyCat, aInParent, aInTargetGname);
        targetRelatorCat = aInTargetRelatorCat;
        addTargetRef(aInTargetGname);
    }

    /**
     * Retrieves target Class.
     * @return  class associated with target name in the context of target class resolution category
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

    /**
     * target relator category accessor
     * @return category used to resolve references to the target class
     */
    protected RelatorCat getTargetRelatorCat()
    {
        return targetRelatorCat;
    }

    /**
     * category used to resolve references to the target class
     */
    private final RelatorCat targetRelatorCat;
}

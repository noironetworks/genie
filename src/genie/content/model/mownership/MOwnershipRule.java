package genie.content.model.mownership;

import genie.engine.model.Cat;
import genie.engine.model.Item;

/**
 * Created by midvorki on 9/27/14.
 */
public class MOwnershipRule extends MOwnershipComponent
{
    protected MOwnershipRule(Cat aInCat, MOwnershipComponent aInParent, String aInName, DefinitionScope aInDefScope)
    {
        super(aInCat,aInParent,aInName, aInDefScope);
    }
}

package genie.content.model.mcont;

import genie.content.model.module.Module;
import genie.engine.model.Cardinality;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import genie.engine.model.RelatorCat;

/**
 * Created by midvorki on 7/8/14.
 */
public abstract class MContRuleItem
		extends MContItem
{
	protected MContRuleItem(
			Cat aInMyCat, MContItem aInParent, RelatorCat aInTargetRelatorCat, String aInTargetGname)
	{
		super(aInMyCat, aInParent, aInTargetRelatorCat, aInTargetGname);
	}
}
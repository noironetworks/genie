package genie.content.model.mcont;

import genie.engine.model.Cat;
import genie.engine.model.RelatorCat;

/**
 * Created by dvorkinista on 7/8/14.
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
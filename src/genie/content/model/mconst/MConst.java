package genie.content.model.mconst;

import genie.content.model.mprop.MProp;
import genie.content.model.mtype.MType;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import modlan.report.Severity;

/**
 * Created by dvorkinista on 7/9/14.
 */
public class MConst extends Item
{
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// CATEGORIES
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static final Cat MY_CAT = Cat.getCreate("mconst");

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTION
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public MConst(
			MProp aInParent,
			String aInName)
	{
		this((Item) aInParent, aInName);
	}

	public MConst(
			MType aInParent,
			String aInName)
	{
		this((Item)aInParent, aInName);
	}

	private MConst(
			Item aInParent,
			String aInName)
	{
		super(MY_CAT, aInParent, aInName);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TYPE API
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public MType getMType(boolean aInIsBaseType)
	{
		Item lParent = getParent();
		MType lType =  lParent instanceof MType ? (MType) lParent : ((MProp) lParent).getType(false);
		if (null == lType)
		{
			Severity.DEATH.report(
					this.toString(),
					"const type retrieval",
					"type definition not found",
					"no type is resolvable for constant in context of " +
			        lParent);
		}
		return aInIsBaseType ? lType.getBase() : lType;
	}


}

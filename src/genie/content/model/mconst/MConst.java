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

	// TODO: ADD SUPPORT FOR
	// TODO:        ACTION: VALUE | REMOVE | TRANSIENT(<target-constant>) |

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
	// SUPER-CONST API
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MConst getSuperConst()
	{
		Item lParent = getParent();
		if (lParent instanceof MType)
		{
			MType lType = (MType) lParent;
			if (!lType.isBase())
			{
				lType = lType.getSupertype();
				if (null != lType)
				{
					lType.findMConst(getLID().getName(), true);
				}
				else
				{
					Severity.DEATH.report(
							this.toString(),
							"retrieval of super const",
							"no supertype",
							"super type can't be found for non-base type " +
							lParent);
				}
			}
		}
		else
		{
			MProp lProp = (MProp) lParent;
			if (!lProp.isBase())
			{
				lProp = lProp.getOverridden(false);
				if (null != lProp)
				{
					lProp.findMConst(getLID().getName(), true);
				}
				else
				{
					Severity.DEATH.report(
							this.toString(),
							"retrieval of super const",
							"no superProp",
							"super Prop can't be found for non-base Prop " +
							lParent);
				}
			}
		}
		return null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// VALUE API
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MValue getValue()
	{
		return (MValue) getChildItem(MValue.MY_CAT,MValue.NAME);
	}

	public MValue findValue(boolean aInCheckSuper)
	{
		MValue lValue = null;
		for (MConst lConst = this;
		     null != lConst && null == lValue;
		     lConst = aInCheckSuper ? lConst.getSuperConst() : null)
		{
			lValue = getValue();
		}
		return lValue;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TYPE API
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public MType getMType(boolean aInIsBaseType)
	{
		Item lParent = getParent();
		MType lType =  lParent instanceof MType ? (MType) lParent : ((MProp) lParent).getMType(false);
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

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PROP API
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MProp getMProp(boolean aInBaseProp)
	{
		Item lParent = getParent();

		if (lParent instanceof MProp)
		{
			return aInBaseProp ? (((MProp)lParent).getBase()) : (MProp) lParent;
		}
		else
		{
			Severity.DEATH.report(
					this.toString(),
					"const property retrievale",
					"no associated property found",
					"const is contained by " +
					lParent);

			return null;
		}
	}
}

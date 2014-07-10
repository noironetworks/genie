package genie.content.model.mconst;

import modlan.report.Severity;

/**
 * Created by dvorkinista on 7/9/14.
 */
public enum ConstAction
{
	MAPPED("mapped"), // MAPS into another constant for value
	VALUE("value"),   // HAS value
	REMOVE("remove"), // removes the constant
	AUTO_REVERTIVE("auto-revertive"), // accepts the value but reverts to some original const
	AUTO_TRANSITION("auto-transition"), // automatically transition to specified const
	DEFAULT("default"), // acts as default value
	UNSETTABLE("unsettable"), // unsettable administratively
	;

	public boolean isRequireIndirectionTargetConst()
	{
		return DEFAULT == this || AUTO_TRANSITION == this  || MAPPED == this;
	}

	public boolean isMapped()
	{
		return DEFAULT == this || MAPPED == this;
	}

	public boolean isDefault()
	{
		return DEFAULT == this;
	}

	private ConstAction(String aIn)
	{
		name = aIn;
	}

	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return name;
	}

	public static ConstAction get(String aIn)
	{
		for (ConstAction lCA : ConstAction.values())
		{
			if (aIn.equalsIgnoreCase(lCA.getName()))
			{
				return lCA;
			}
		}
		Severity.DEATH.report(
				"ConstAction",
				"get const action for name",
				"no such const action",
				"no support for " + aIn + "; actions supported: " + ConstAction.values());

		return null;
	}
	private final String name;
}

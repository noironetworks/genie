package genie.content.model.mconst;

import modlan.report.Severity;

/**
 * Created by dvorkinista on 7/9/14.
 */
public enum ConstAction
{
	VALUE("value"),
	REMOVE("remove"),
	TRANSIENT("transient"),
	;

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

package genie.content.model.mprop;

import modlan.report.Severity;

/**
 * Created by midvorki on 7/7/14.
 */
public enum PropAction
{
	DEFINE("define"),
	OVERRIDE("override"),
	HIDE("hide"),
	;
	private PropAction(String aIn)
	{
		name = aIn;
	}

	public boolean isOverride()
	{
		return DEFINE != this;
	}

	public boolean isHide()
	{
		return HIDE == this;
	}

	public boolean isDefine()
	{
		return DEFINE == this;
	}

	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return name;
	}

	public static PropAction get(String aIn)
	{
		for (PropAction lPA : PropAction.values())
		{
			if (aIn.equalsIgnoreCase(lPA.getName()))
			{
				return lPA;
			}
		}
		Severity.DEATH.report(
				"PropertyAction",
				"get property action for name",
				"no such property action",
				"no support for " + aIn + "; actions supported: " + PropAction.values());

		return null;
	}
	private final String name;
}

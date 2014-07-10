package genie.content.model.mconst;

import genie.content.model.mtype.MType;
import genie.engine.model.Cat;
import genie.engine.model.Item;

/**
 * Created by midvorki on 7/9/14.
 */
public class MValue extends Item
{
	public static final Cat MY_CAT = Cat.getCreate("mconst:value");
	public static final String NAME = "value";

	public MValue(MConst aInConst, String aInValue)
	{
		super(MY_CAT,aInConst,NAME);
		value = aInValue;
	}

	public String getValue()
	{
		return value;
	}

	public MConst getMConst()
	{
		return (MConst) getParent();
	}

	public MType getMType()
	{
		return getMConst().getMType(false);
	}

	public MType getBaseMType()
	{
		return getMType().getBase();
	}

	private String value;
}

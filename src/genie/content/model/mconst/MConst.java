package genie.content.model.mconst;

import genie.content.model.mprop.MProp;
import genie.content.model.mtype.MType;
import genie.engine.model.Cat;
import genie.engine.model.Item;

/**
 * Created by midvorki on 7/9/14.
 */
public class MConst extends Item
{
	public static final Cat MY_CAT = Cat.getCreate("mconst");

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
}

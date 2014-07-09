package genie.content.model.mtype;

import genie.engine.model.Cat;

/**
 * Created by midvorki on 7/7/14.
 */
public class TypeHint extends SubTypeItem
{
	public static final Cat MY_CAT = Cat.getCreate("type:hint");
	public static final String NAME = "hint";

	public TypeHint(
			MType aInType)
	{
		super(MY_CAT, aInType, NAME);
	}
}

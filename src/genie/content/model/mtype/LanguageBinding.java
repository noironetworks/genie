package genie.content.model.mtype;

import genie.engine.model.Cat;

/**
 * Created by midvorki on 7/7/14.
 */
public class LanguageBinding extends SubTypeItem
{
	public static final Cat MY_CAT = Cat.getCreate("type:language-binding");

	public LanguageBinding(
			MType aInType, Language aInLang)
	{
		super(MY_CAT, aInType, aInLang.getName());
	}

}

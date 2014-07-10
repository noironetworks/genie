package genie.content.model.mvalidation;

import genie.engine.model.Cat;
import genie.engine.model.Item;

/**
 * Created by midvorki on 7/10/14.
 */
public class MContentValidatorParam
		extends Item
{
	public static final Cat MY_CAT = Cat.getCreate("mvalidator:mcontent:param");

	public MContentValidatorParam(
			MContentValidator aInContentValidator, String aInName, String aInValue, String aInTypeGName)
	{
		super(MY_CAT,aInContentValidator,aInName);
		value = aInValue;
	}
	// HAS VALUE AND TYPE
	private final String value;
}

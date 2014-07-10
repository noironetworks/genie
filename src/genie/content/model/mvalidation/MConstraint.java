package genie.content.model.mvalidation;

import genie.engine.model.Cat;
import genie.engine.model.Item;
import modlan.report.Severity;

/**
 * Created by midvorki on 7/10/14.
 */
public class MConstraint extends Item
{
	protected MConstraint(Cat aInCat, MValidator aInValidator, String aInName, ValidatorAction aInAction)
	{
		super(aInCat, aInValidator, aInName);
		if (ValidatorAction.REMOVE == aInValidator.getAction())
		{
			Severity.DEATH.report(
					this.toString(),
					"constraint definition",
					"constraint can't be defined",
					"constraint can only be defined under non-removed validators: " + aInValidator.getAction());
		}
		action = (null == aInAction) ?
					(ValidatorAction.ADD == aInValidator.getAction() ?
							                ValidatorAction.CLOBBER : aInValidator.getAction()) :
					(ValidatorAction.ADD == aInAction ?
							ValidatorAction.CLOBBER : aInAction);

	}

	public ValidatorAction getAction()
	{
		return action;
	}
	
	private final ValidatorAction action;
}

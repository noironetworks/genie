package genie.content.model.mprop;

import genie.content.model.mclass.MClass;
import genie.content.model.mclass.SubStructItem;
import genie.content.model.mtype.MType;
import genie.engine.model.Cardinality;
import genie.engine.model.Cat;
import genie.engine.model.Relator;
import genie.engine.model.RelatorCat;
import modlan.report.Severity;

import java.util.Collection;

/**
 * Created by midvorki on 7/7/14.
 */
public class MProp extends SubStructItem
{
	public static final Cat MY_CAT = Cat.getCreate("mprop");
	public static final RelatorCat TYPE_CAT = RelatorCat.getCreate("prop:type", Cardinality.SINGLE);

	public MProp(
			MClass aInParent,
			String aInLName,
			PropAction aInAction)
	{
		super(MY_CAT, aInParent, aInLName);
		action = aInAction;
	}

	public static MProp get(String aInGName)
	{
		return (MProp) MY_CAT.getItem(aInGName);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PROPERTY ACTION APIs
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * retrieves property action that represents whether this property is a property definition, override or a hide.
	 * @return property action
	 */
	public PropAction getAction()
	{
		return action;
	}

	/**
	 * determines of this property is override
	 * @return true if this property is override, false otherwise
	 */
	public boolean isOverride()
	{
		return action.isOverride();
	}

	/**
	 * determines if this property is override implying hiding.
	 * @return if this property is to be hidden.
	 */
	public boolean isHide()
	{
		return action.isHide();
	}

	/**
	 * Determines if this property is the original base definition.
	 * Synonymous with isBase().
	 * @return true if this property is the original base definition
	 */
	public boolean isDefine()
	{
		return action.isDefine();
	}

	/**
	 * Determines if this property is the original base definition.
	 * Synonymous with isDefine().
	 * @return true if this property is the original base definition
	 */
	public boolean isBase()
	{
		return isDefine();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TYPE APIs
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Associates type with the property. Only works with base property definitions (BY DESIGN - MD)
	 * @param aInTypeGName global name of the type
	 */
	public void addType(String aInTypeGName)
	{
		if (isBase())
		{
			TYPE_CAT.add(MY_CAT, getGID().getName(), MType.MY_CAT, aInTypeGName);
		}
		else
		{
			Severity.DEATH.report(
					this.toString(),
					"property type registration",
					"can't override type",
					"can't specify type in property override:  " + aInTypeGName);
		}
	}

	/**
	 * retrieves relator representing type relationship
	 * @return relator that represents type relationship, null if doesn't exist
	 */
	public Relator getTypeRelator()
	{
		return TYPE_CAT.getRelator(getGID().getName());
	}

	/**
	 * Retrieves this property's data type.
	 * @param aInIsBaseType specifies whether to retrieve the base type or the immediate type that the base property uses
	 * @return property data type
	 */
	public MType getType(boolean aInIsBaseType)
	{
		MProp lBaseProp = getBase();
		Relator lRel = lBaseProp.getTypeRelator();
		MType lType = (MType) (null == lRel ? null : lRel.getToItem());
		if (null == lType)
		{
			Severity.DEATH.report(
					this.toString(),
					"property type retrieval",
					"can't retrieve property type",
					"base property definition " + lBaseProp +
					" does not have type defined: all base property definitions required to have a type:" +
			        " no defaults assumed");
		}
		return aInIsBaseType ? lType.getBase() : lType;
	}

	/**
	 * Retrieves all types, including all the subtypes... Added in order of distance to this property
	 * @param aOut types found
	 */
	public void getTypes(Collection<MType> aOut)
	{
		getType(false).getSupertypes(aOut,true);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PROPERTY INHERITANCE/OVERRIDE APIs
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * retrieves base property definition
	 * @return base property for this property, if this is a base/definition property
	 */
	public MProp getBase()
	{
		return isBase() ? this : getOverridden(true);
	}

	/**
	 * retrieves the property that this property overrides
	 * @param aInIsBase specifies if only the base definition is to be found. If false, the closest def is found
	 * @return property that this property overrides
	 */
	public MProp getOverridden(boolean aInIsBase)
	{
		MProp lRet = null;
		if (!isBase())
		{
			for (MClass lThisClass = getMClass().getSuperclass();
			     null != lThisClass;
			     lThisClass = lThisClass.getSuperclass())
			{
				lRet = lThisClass.getProp(getLID().getName());
				if (null != lRet && (lRet.isBase()) || (!aInIsBase)) // if we need base and this is base OR we don't care about base
				{
					return lRet;
				}
			}
			Severity.DEATH.report(
					this.toString(),
					"override target retrieval",
					"no target found",
					"no base definition is found for prop " + getLID().getName());
		}
		return lRet;
	}

	/**
	 * retrieves the property definition vector of property overrides and defs that this property overrides
	 * (or further overrides)
	 * @param aOut a collection of found overrides. items are added on order of superclass traversal according to distance away from this property
	 */
	public void getOverridden(Collection<MProp> aOut)
	{
		if (!isBase())
		{
			boolean lFound = false;
			for (MClass lThisClass = getMClass().getSuperclass();
			     null != lThisClass;
			     lThisClass = lThisClass.getSuperclass())
			{
				MProp lProp = lThisClass.getProp(getLID().getName());
				if (null != lProp)
				{
					lFound = true;
					aOut.add(lProp);
				}
			}
			if (!lFound)
			{
				Severity.DEATH.report(
						this.toString(),
						"override target retrieval",
						"no target found",
						"no base definition is found for prop " + getLID().getName());
			}
		}
	}

	/**
	 * retrieves all properties that override this property
	 * @param aOut collection of overriding properties
	 */
	public void getOverriders(Collection<MProp> aOut)
	{
		for (MClass lThisClass : getMClass().getSubclasses(false))
		{
			MProp lThisProp = lThisClass.getProp(getLID().getName());
			if (null != lThisProp)
			{
				aOut.add(lThisProp);
			}
		}
	}

	private final PropAction action;
}

package genie.content.model.mclass;

import genie.content.model.mcont.MChild;
import genie.content.model.mcont.MContained;
import genie.content.model.mcont.MContainer;
import genie.content.model.mcont.MParent;
import genie.content.model.module.Module;
import genie.content.model.module.SubModuleItem;
import genie.content.model.mprop.MProp;
import genie.engine.model.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by dvorkinista on 7/6/14.
 *
 * This class represents a managed class model item.
 */
public class MClass
		extends SubModuleItem
{

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// CATEGORIES
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static final Cat MY_CAT = Cat.getCreate("mclass");
	public static final RelatorCat SUPER_CAT = RelatorCat.getCreate("superclass", Cardinality.SINGLE);

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTION
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MClass(
			Module aInModule, String aInLName, boolean aInIsConcrete
	             )
	{
		super(MY_CAT, aInModule, aInLName);
		isConcrete = aInIsConcrete;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// CLASS RETRIEVAL APIs
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static MClass get(String aInGName)
	{
		return (MClass) MY_CAT.getItem(aInGName);
	}

	/**
	 * retrieves containing class for a sub-class item
	 * @param aIn sub-class item like property etc..
	 * @return containing class
	 */
	public static MClass getMClass(Item aIn)
	{
		return (MClass) aIn.getAncestorOfCat(MClass.MY_CAT);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// CONCRETENESS API
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * specifies whether this managed class if concrete
	 * @return true if class is concrete, false if abstract
	 */
	public boolean isConcrete()
	{
		return isConcrete;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// INHERITANCE API
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * registers superclass for this class
	 * @param aInTargetGName super class global name
	 */
	public void addSuperclass(String aInTargetGName)
	{
		SUPER_CAT.add(MY_CAT, getGID().getName(), MY_CAT, aInTargetGName);
	}

	/**
	 * checks if the class has a superclass
	 * @return returns true if this class has superclass, false otherwise
	 */
	public boolean hasSuperclass()
	{
		Relator lRel = SUPER_CAT.getRelator(getGID().getName());
		return null != lRel && lRel.hasTo();
	}

	/**
	 * retrieves relator representing superclass relationship
	 * @return relator that represents superclass relationship with another class, null if doesn't exist
	 */
	public Relator getSuperclassRelator()
	{
		return SUPER_CAT.getRelator(getGID().getName());
	}

	/**
	 * retrieves superclass of this class
	 * @return superclass if superclass exists, null otherwise
	 */
	public MClass getSuperclass()
	{
		Relator lRel = getSuperclassRelator();
		return (MClass) (null == lRel ? null : lRel.getToItem());
	}

	/**
	 * retrieves all superclasses for this class. superclasses are added in order of distance from the subclass.
	 * @param aOut collection of superclasses
	 */
	public void getSuperclasses(Collection<MClass> aOut)
	{
		for (MClass lThat = getSuperclass(); null != lThat; lThat = lThat.getSuperclass())
		{
			aOut.add(lThat);
		}
	}

	/**
	 * checks if this class has subclasses
	 * @return true if this class has subclasses. false otherwise.
	 */
	public boolean hasSubclasses()
	{
		Relator lInvRel = SUPER_CAT.getInverseRelator(getGID().getName());
		return null != lInvRel && lInvRel.hasTo();
	}

	/**
	 * Retrieves subclasses of this class
	 * @param aOut collection of returned subclasses of this class
	 * @param aInIsDirectOnly specifies if only direct subclasses are returned
	 * @param aInIsConcreteOnly specifies whether to return concrete classes only
	 */
	public void getSubclasses(
			Collection<MClass> aOut,
			boolean aInIsDirectOnly,
			boolean aInIsConcreteOnly)
	{
		Relator lInvRel = SUPER_CAT.getInverseRelator(getGID().getName());
		if (null != lInvRel)
		{
			for (Item lItem : lInvRel.getToItems())
			{
				MClass lClass = (MClass) lItem;
				if ((!aInIsConcreteOnly) || lClass.isConcrete())
				{
					aOut.add(lClass);
				}
				if (!(aInIsDirectOnly || lClass.isConcrete()))
				{
					lClass.getSubclasses(aOut,aInIsDirectOnly, aInIsConcreteOnly);
				}
			}
		}
	}

	/**
	 * Retrieves subclasses of this class
	 * @param aInIsDirectOnly specifies if only direct subclasses are returned
	 * @param aInIsConcreteOnly specifies whether to return concrete classes only
	 * @return collection of subclasses of this class
	 */
	public Collection<MClass> getSubclasses(
			boolean aInIsDirectOnly,
			boolean aInIsConcreteOnly
	        )
	{
		LinkedList<MClass> lRet = new LinkedList<MClass>();
		getSubclasses(lRet, aInIsDirectOnly, aInIsConcreteOnly);
		return lRet;
	}

	/**
	 * Retrieves subclasses of this class in the inheritance tree
	 * @param aOut collection of subclasses of this class
	 * @param aInIsConcreteOnly specifies whether to return concrete classes only
	 */
	public void getSubclasses(Collection<MClass> aOut, boolean aInIsConcreteOnly)
	{
		getSubclasses(aOut,false,aInIsConcreteOnly);
	}

	/**
	 * Retrieves subclasses of this class in the inheritance tree
	 * @param aInIsConcreteOnly specifies whether to return concrete classes only
	 * @return collection of subclasses of this class
	 */
	public Collection<MClass> getSubclasses(boolean aInIsConcreteOnly)
	{
		LinkedList<MClass> lRet = new LinkedList<MClass>();
		getSubclasses(lRet, false, aInIsConcreteOnly);
		return lRet;
	}

	/**
	 * Retrieves direct subclasses of this class in the inheritance tree
	 * @param aOut collection of direct subclasses of this class
	 * @param aInIsConcreteOnly specifies whether to return concrete classes only
	 */
	public void getDirectSubclasses(Collection<MClass> aOut, boolean aInIsConcreteOnly)
	{
		getSubclasses(aOut,true,aInIsConcreteOnly);
	}

	/**
	 * Retrieves direct subclasses of this class in the inheritance tree
	 * @param aInIsConcreteOnly specifies whether to return concrete classes only
	 * @return collection of direct subclasses of this class
	 */
	public Collection<MClass> getDirectSubclasses(boolean aInIsConcreteOnly)
	{
		LinkedList<MClass> lRet = new LinkedList<MClass>();
		getSubclasses(lRet, true, aInIsConcreteOnly);
		return lRet;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// CONTAINMENTS APIs
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Gets the rule item that represents who contains by this class.
	 * @return containment rule item representing who contains this class
	 */
	public MContained getContained()
	{
		return MContained.get(getGID().getName());
	}

	/**
	 * represents whether any class contains this class; i.e. whether this class has any containment rules.
	 */
	public boolean hasContained()
	{
		MContained lCont = getContained();
		return null != lCont && lCont.hasParents();
	}

	/**
	 * Gets a set of rules that describe who contains this object
	 * @param aOut set of containment rules
	 */
	public void getContainedBy(Collection<MParent> aOut)
	{
		MContained lCont = getContained();
		if (null != lCont)
		{
			lCont.getParents(aOut);
		}
	}

	/**
	 * Gets a set of rules that describe who contains this object or any of its supeclasses
	 * @param aOut set of containment rules\
	 * @param aInIncludeSuperclasses indication of whether to include superclasses in this search
	 */
	public void getContainedBy(Collection<MParent> aOut, boolean aInIncludeSuperclasses)
	{
		getContainedBy(aOut);
		if (aInIncludeSuperclasses)
		{
			for (MClass lClass = getSuperclass(); null != lClass; lClass = lClass.getSuperclass())
			{
				lClass.getContainedBy(aOut);
			}
		}
	}

	/**
	 * Gets a set of Classes that contain this class
	 * @param aOut collection of containing classes
	 */
	public void getContainedByClasses(Collection<MClass> aOut)
	{
		MContained lCont = getContained();
		if (null != lCont)
		{
			lCont.getParentClasses(aOut);
		}
	}

	/**
	 * Gets a set of Classes that contain this class
	 * @param aOut collection of containing classes
	 * @param aInIncludeSuperclasses indication of whether to include superclasses in this search
	 */
	public void getContainedByClasses(Collection<MClass> aOut, boolean aInIncludeSuperclasses)
	{
		getContainedByClasses(aOut);
		if (aInIncludeSuperclasses)
		{
			for (MClass lClass = getSuperclass(); null != lClass; lClass = lClass.getSuperclass())
			{
				lClass.getContainedByClasses(aOut);
			}
		}
	}

	/**
	 * Gets the rule item that represents who is contained by this class.
	 * @return containment rule item representing who is contained by this class
	 */
	public MContainer getContains()
	{
		return MContainer.get(getGID().getName());
	}

	/**
	 * represents whether any class is contained by this class; i.e. whether this class has any contained-by rules.
	 */
	public boolean hasContains()
	{
		MContainer lCont = getContains();
		return null != lCont && lCont.hasChildren();
	}

	/**
	 * Gets a set of rules that describe who this object contains
	 * @param aOut set of containment rules
	 */
	public void getContains(Collection<MChild> aOut)
	{
		MContainer lCont = getContains();
		if (null != lCont)
		{
			lCont.getChildren(aOut);
		}
	}

	/**
	 * Gets a set of rules that describe who is contained by this class or any of its supeclasses
	 * @param aOut set of containment rules
	 * @param aInIncludeSuperclasses indication of whether to include superclasses in this search
	 */
	public void getContains(Collection<MChild> aOut, boolean aInIncludeSuperclasses)
	{
		getContains(aOut);
		if (aInIncludeSuperclasses)
		{
			for (MClass lClass = getSuperclass(); null != lClass; lClass = lClass.getSuperclass())
			{
				lClass.getContains(aOut);
			}
		}
	}

	/**
	 * Gets a set of classes that are contained by this class
	 * @param aOut collection of contained classes
	 */
	public void getContainsClasses(Collection<MClass> aOut)
	{
		MContainer lCont = getContains();
		if (null != lCont)
		{
			lCont.getChildClasses(aOut);
		}
	}

	/**
	 * Gets a set of classes that are contained by this class or any of its supeclasses
	 * @param aOut set of containment rules\
	 * @param aInIncludeSuperclasses indication of whether to include superclasses in this search
	 */
	public void getContainsClasses(Collection<MClass> aOut, boolean aInIncludeSuperclasses)
	{
		getContainsClasses(aOut);
		if (aInIncludeSuperclasses)
		{
			for (MClass lClass = getSuperclass(); null != lClass; lClass = lClass.getSuperclass())
			{
				lClass.getContainsClasses(aOut);
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PROPERTY APIs
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Retrieves property from this class. does not check superclasses.
	 * @param aInName name of the property retrieved
	 * @return property defined under this class
	 */
	public MProp getProp(String aInName)
	{
		return (MProp) getChildItem(MProp.MY_CAT, aInName);
	}

	/**
	 * Retrieves all properties that this given class has. This method does not inspect superclasses.
	 * @param aOut found properties
	 * @param aInIsBaseOnly indicates that only base property definitions are to be included.
	 */
	public void getProp(Map<String, MProp> aOut, boolean aInIsBaseOnly)
	{
		LinkedList<Item> lProps = new LinkedList<Item>();
		getChildItems(MProp.MY_CAT, lProps);
		for (Item lItem : lProps)
		{
			MProp lProp = (MProp) lItem;
			if (!aInIsBaseOnly || lProp.isBase())
			{
				if (!aOut.containsKey(lProp.getLID().getName()))
				{
					aOut.put(lProp.getLID().getName(), lProp);
				}
			}
		}
	}

	/**
	 * Finds a property by passed in name in this class or any of its superclasses.
	 * @param aInName name of the property to be found
	 * @param aInIsBase specifies if only the base property is to be found
	 * @return property corresponding to the name passed in
	 */
	public MProp findProp(String aInName, boolean aInIsBase)
	{
		MProp lThisProp = null;
		for (MClass lThisClass = this;
		     null != lThisClass && null == lThisProp;
		     lThisClass = lThisClass.getSuperclass())
		{
			lThisProp = lThisClass.getProp(aInName);
		}
		return null == lThisProp ? null : (aInIsBase ? lThisProp.getBase() : lThisProp);
	}

	/**
	 * Finds all properties of this class, and all of its superclasses.
	 * @param aOut found properties
	 * @param aInIsBaseOnly indicates that only base property definitions are to be included.
	 */
	public void findProp(Map<String, MProp> aOut, boolean aInIsBaseOnly)
	{
		for (MClass lThisClass = this;
		     null != lThisClass;
		     lThisClass = lThisClass.getSuperclass())
		{
			lThisClass.getProp(aOut,aInIsBaseOnly);
		}
	}

	private final boolean isConcrete;
}

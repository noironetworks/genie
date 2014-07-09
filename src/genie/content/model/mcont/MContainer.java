package genie.content.model.mcont;

import genie.content.model.mclass.MClass;
import genie.content.model.module.Module;
import genie.engine.model.*;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by midvorki on 7/8/14.
 *
 * Specifies "container" rule set for a given class. These rules are expressed from the vantage point of the
 * container class on the managed information tree.
 *
 * Containment is a relationship between two classes. Each containment rule identifies that the child class can be
 * contained on the managed information tree by the parent class. In addition containment rule holds directives on
 * lifecycle control as well as directive affecting relative naming.
 *
 * CAT[mcont:mcontainer]->MContainer->MChild
 *
 * Containment relationships can't be instantiated via direct construction, use MContained.addRule(...) method.
 */
public class MContainer
		extends MContItem
{
	public static final Cat MY_CAT = Cat.getCreate("mcont:mcontainer");
	public static final RelatorCat TARGET_CAT = RelatorCat.getCreate("mcont:mcontainer:childref", Cardinality.SINGLE);

	static final MContainer addRule(String aInParentGName, String aInChildGName)
	{
		MContainer lContr = MContainer.get(aInParentGName, true);
		lContr.getMChild(aInChildGName, true);

		return lContr;
	}

	MContainer(String aInParentGname)
	{
		super(MY_CAT, null, TARGET_CAT, aInParentGname);
	}

	public static MContainer get(String aInGName)
	{
		return (MContainer) MY_CAT.getItem(aInGName);
	}

	public static MContainer get(String aInGName, boolean aInCreateIfNotFound)
	{
		MContainer lContr = get(aInGName);
		if (null == lContr && aInCreateIfNotFound)
		{
			synchronized (MY_CAT)
			{
				lContr = get(aInGName);
				if (null != lContr)
				{
					lContr = new MContainer(aInGName);
				}
			}
		}
		return lContr;
	}

	/**
	 * checks if there are per child rules
	 */
	public boolean hasChild()
	{
		return hasChildren(MChild.MY_CAT);
	}

	public void getChildren(Collection<MChild> aOut)
	{
		LinkedList<Item> lItems = new LinkedList<Item>();
		getChildItems(MChild.MY_CAT, lItems);
		for (Item lIt : lItems)
		{
			aOut.add((MChild)lIt);
		}
	}

	public MChild getMChild(String aInClassGName)
	{
		return (MChild) getChildItem(MChild.MY_CAT,aInClassGName);
	}

	public MChild getMChild(String aInClassGName, boolean aInCreateIfNotFound)
	{
		MChild lMChild = getMChild(aInClassGName);
		if (null == lMChild && aInCreateIfNotFound)
		{
			synchronized (this)
			{
				lMChild = getMChild(aInClassGName);
				if (null == lMChild)
				{
					lMChild = new MChild(this, aInClassGName);
				}
			}
		}
		return lMChild;
	}

	public void getChildClasses(Collection<MClass> aOut)
	{
		LinkedList<Item> lItems = new LinkedList<Item>();
		getChildItems(MParent.MY_CAT, lItems);
		for (Item lIt : lItems)
		{
			aOut.add(((MParent)lIt).getTarget());
		}
	}
}

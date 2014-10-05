package genie.content.model.mclass;

import genie.content.model.mcont.MChild;
import genie.content.model.mcont.MContained;
import genie.content.model.mcont.MContainer;
import genie.content.model.mcont.MParent;
import genie.content.model.mnaming.MNameComponent;
import genie.content.model.mnaming.MNameRule;
import genie.content.model.mnaming.MNamer;
import genie.content.model.module.Module;
import genie.content.model.module.SubModuleItem;
import genie.content.model.mownership.MOwned;
import genie.content.model.mownership.MOwner;
import genie.content.model.mprop.MProp;
import genie.content.model.mprop.MPropGroup;
import genie.content.model.mtype.Language;
import genie.content.model.mtype.MType;
import genie.engine.model.*;
import modlan.report.Severity;
import modlan.utils.Strings;

import java.util.*;

/**
 * Created by dvorkinista on 7/6/14.
 *
 * This class represents a managed class model item.
 */
public class MClass
        extends SubModuleItem
{
    public static final String ROOT_CLASS_GNAME = "dmtree/Root";

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
    public static MClass getClass(Item aIn)
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

    public String getFullConcatenatedName()
    {
        return Strings.upFirstLetter(getModule().getLID().getName()) + Strings.upFirstLetter(getLID().getName());
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
     * @param aOut collection of returned subclasses of this class
     * @param aInIsDirectOnly specifies if only direct subclasses are returned
     * @param aInIsConcreteOnly specifies whether to return concrete classes only
     */
    public void getSubclasses(
            Map<Ident, MClass> aOut,
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
                    aOut.put(lClass.getGID(),lClass);
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

    public boolean isInstanceOf(String aInSuperClassGlobalName)
    {
        return getGID().getName().equals(aInSuperClassGlobalName) ||
               (hasSuperclass() && getSuperclass().isInstanceOf(aInSuperClassGlobalName));
    }

    public boolean isSubclassOf(String aInSuperClassGlobalName)
    {
        return (hasSuperclass() && getSuperclass().isInstanceOf(aInSuperClassGlobalName));
    }

    public boolean isConcreteSuperclassOf(String aInSuperClassGlobalName)
    {
        return isConcrete() && isSubclassOf(aInSuperClassGlobalName);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONTAINMENTS APIs
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * containment root accessor: dmtree/Root
     * @return containment root
     */
    public static MClass getContainmentRoot()
    {
        return get(ROOT_CLASS_GNAME);
    }

    /**
     * determines whether this class is a root of containment hiercrhy
     * @return true if this class is the root of containment hierarchy.
     */
    public boolean isRoot() { return getContainmentRoot() == this; }

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
    public void getContainedByClasses(Map<Ident,MClass> aOut, boolean aInIsResolveToConcrete)
    {
        MContained lCont = getContained();
        if (null != lCont)
        {
            lCont.getParentClasses(aOut, aInIsResolveToConcrete);
        }
    }

    /**
     * Gets a set of Classes that contain this class
     * @param aOut collection of containing classes
     * @param aInIncludeSuperclasses indication of whether to include superclasses in this search
     */
    public void getContainedByClasses(Map<Ident,MClass> aOut, boolean aInIncludeSuperclasses, boolean aInIsResolveToConcrete)
    {
        getContainedByClasses(aOut, aInIsResolveToConcrete);
        if (aInIncludeSuperclasses)
        {
            for (MClass lClass = getSuperclass(); null != lClass; lClass = lClass.getSuperclass())
            {
                lClass.getContainedByClasses(aOut, aInIsResolveToConcrete);
            }
        }
    }

    public Collection<MClass> getContainedByClasses(boolean aInIncludeSuperclasses, boolean aInIsResolveToConcrete)
    {
        TreeMap<Ident,MClass> lOut = new TreeMap<Ident, MClass>();
        getContainedByClasses(lOut, aInIncludeSuperclasses, aInIsResolveToConcrete);
        return lOut.values();
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
     * @param aInIsResolveToConcrete specifies if contained classes should be resolved to concrete
     */
    public void getContainsClasses(Map<Ident,MClass> aOut, boolean aInIsResolveToConcrete)
    {
        //System.out.println(this + ".getContains()");
        MContainer lCont = getContains();
        if (null != lCont)
        {
            lCont.getChildClasses(aOut, aInIsResolveToConcrete);
        }
        /**
        else
        {
            Severity.WARN.report(toString(), "retrieval of contained classes", "no container", "container not found");
        }
         **/
    }

    /**
     * Gets a set of classes that are contained by this class or any of its supeclasses
     * @param aOut set of containment rules\
     * @param aInIncludeSuperclasses indication of whether to include superclasses in this search
     * @param aInIsResolveToConcrete specifies if contained classes should be resolved to concrete
     */
    public void getContainsClasses(Map<Ident, MClass> aOut, boolean aInIncludeSuperclasses, boolean aInIsResolveToConcrete)
    {
        getContainsClasses(aOut, aInIsResolveToConcrete);
        if (aInIncludeSuperclasses)
        {
            for (MClass lClass = getSuperclass(); null != lClass; lClass = lClass.getSuperclass())
            {
                lClass.getContainsClasses(aOut, aInIsResolveToConcrete);
            }
        }
    }

    public Collection<List<MClass>> getContainmentPaths()
    {
        LinkedList<List<MClass>> lRet = new LinkedList<List<MClass>>();
        getContainmentPaths(lRet);
        return lRet;
    }

    public void getContainmentPaths(Collection<List<MClass>> aOut)
    {
        if (isConcrete())
        {
            Stack<MClass> lStack = new Stack<MClass>();
            exploreContainmentPaths(lStack, aOut);
        }
    }

    private void exploreContainmentPaths(Stack<MClass> aInCurrStack, Collection<List<MClass>> aOut)
    {
        if (isRoot())
        {
            // WE STUMPLED INTO ROOT FOR THIS PATH,
            // WE'RE AT THE END OF THE PATH:
            // LET'S ADD THIS PATH
            LinkedList<MClass> lPath = new LinkedList<MClass>();
            //lPath.addAll(aInCurrStack);

            for (MClass lThis : aInCurrStack)
            {
                lPath.addFirst(lThis);
            }

            aOut.add(lPath);
        }

        else if (isConcrete())
        {
            aInCurrStack.push(this);
            Map<Ident,MClass> lContainers = new TreeMap<Ident, MClass>();
            getContainedByClasses(lContainers, true, true);
            if (0 == lContainers.size())
            {
                // NO MORE CONTAINERS,
                // LET'S ASSUME ROOT
                getContainmentRoot().exploreContainmentPaths(aInCurrStack, aOut);
            }
            else
            {
                for (MClass lThis : lContainers.values())
                {
                    lThis.exploreContainmentPaths(aInCurrStack, aOut);
                }
            }
            if (aInCurrStack.peek() == this)
            {
                aInCurrStack.pop();
            }
            else
            {
                Severity.DEATH.report(toString(), "containment path calculation", "",
                                      "trying to pop... unexpected class: " + aInCurrStack.peek().getGID().getName() + " ::: " + aInCurrStack);
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
     * internal method to retrieve all properties of this given class that match the group passed in and not contained in the exclusion list passed in
     * @param aOut found properties
     * @param aInOutExcluded exclusion list
     * @param aInGroup name of the property group for which properties are retrieved
     */
    private void getProp(Map<String, MProp> aOut, Collection<String> aInOutExcluded, String aInGroup)
    {
        LinkedList<Item> lProps = new LinkedList<Item>();
        getChildItems(MProp.MY_CAT, lProps);
        for (Item lItem : lProps)
        {
            MProp lProp = (MProp) lItem;
            if (!aInOutExcluded.contains(lProp.getLID().getName()))
            {
                if (!aOut.containsKey(lProp.getLID().getName()))
                {
                    if (lProp.getGroup().equals(aInGroup))
                    {
                        aOut.put(lProp.getLID().getName(), lProp);
                    }
                    else
                    {
                        aInOutExcluded.add(lProp.getLID().getName());
                    }
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

    /**
     * finds all properties of the class that belong to a property group passed in
     * @param aOut properties that match the group
     * @param aInGroup group for which properties are being retrieved
     */
    public void findProp(Map<String, MProp> aOut, String aInGroup)
    {
        TreeSet<String> lExcluded = new TreeSet<String>();
        for (MClass lThisClass = this;
             null != lThisClass;
             lThisClass = lThisClass.getSuperclass())
        {
            lThisClass.getProp(aOut,lExcluded,aInGroup);
        }
    }

    public int countMyDefinedProps()
    {
        if (-1 == numOfBaseProps)
        {
            numOfBaseProps = 0;

            Children lChildren = this.getNode().getChildren();
            if (null != lChildren)
            {
                CatEntry lCatE = lChildren.getEntry(MProp.MY_CAT);
                if (null != lCatE)
                {
                    for (Node lNode : lCatE.getList())
                    {
                        MProp lThisProp = (MProp) lNode.getItem();
                        if (null != lThisProp)
                        {
                            if (lThisProp.isBase())
                            {
                                numOfBaseProps++;
                            }
                        }
                    }
                }
            }
        }
        return numOfBaseProps;
    }

    public int countProps()
    {
        if (-1 == numOfAllBaseProps)
        {
            numOfAllBaseProps = 0;
            for (MClass lThis = this; null != lThis; lThis = lThis.getSuperclass())
            {
                numOfAllBaseProps += lThis.countMyDefinedProps();
            }
        }
        return numOfAllBaseProps;
    }

    public int countInheritedProps()
    {
        return hasSuperclass() ? getSuperclass().countProps() : 0;
    }

    private void initPropLocalIdx()
    {
        int lCurrIdx = countInheritedProps();

        Children lChildren = this.getNode().getChildren();
        if (null != lChildren)
        {
            CatEntry lCatE = lChildren.getEntry(MProp.MY_CAT);
            if (null != lCatE)
            {
                for (Node lNode : lCatE.getList())
                {
                    MProp lThisProp = (MProp) lNode.getItem();
                    if (null != lThisProp)
                    {
                        if (lThisProp.isBase())
                        {
                            lThisProp.setLocalIdx(++lCurrIdx);
                        }
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PROPERTY GROUP APIs
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public synchronized MPropGroup getPropGroup(String aIn, boolean aInCreateIfDoesnExist)
    {
        MPropGroup lGroup = getPropGroup(aIn);
        if (null == lGroup)
        {
            lGroup = new MPropGroup(this,aIn);
        }
        return lGroup;
    }

    public MPropGroup getPropGroup(String aIn)
    {
        return (MPropGroup) getChildItem(MPropGroup.MY_CAT,aIn);
    }

    public void getPropGroup(Map<String, MPropGroup> aOut)
    {
        LinkedList<Item> lItems = new LinkedList<Item>();
        getChildItems(MPropGroup.MY_CAT,lItems);
        for (Item lIt : lItems)
        {
            if (!aOut.containsKey(lIt.getLID().getName()))
            {
                aOut.put(lIt.getLID().getName(), (MPropGroup) lIt);
            }
        }
    }

    public MPropGroup findGroup(String aIn)
    {
        MPropGroup lRet = null;
        for (MClass lThisClass = this;
             null != lThisClass && null == lRet;
             lThisClass = lThisClass.getSuperclass())
        {
            lRet = lThisClass.getPropGroup(aIn);
        }
        if (null == lRet)
        {
            Severity.DEATH.report(this.toString(),"prop group retrieval", "prop group not found", "no prop group with name " + aIn);
        }
        return lRet;
    }

    public void findGroup(Map<String, MPropGroup> aOut)
    {
        for (MClass lThisClass = this;
             null != lThisClass;
             lThisClass = lThisClass.getSuperclass())
        {
            lThisClass.getPropGroup(aOut);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OWNER APIs
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addOwner(String aInOwner)
    {
        if (null == getChildItem(MOwned.MY_CAT, aInOwner))
        {
            new MOwned(this, aInOwner);
        }
    }

    public void findOwned(TreeMap<String, MOwned> aOut)
    {
        LinkedList<Item> ll = new LinkedList<Item>();
        for (MClass lThis = this; lThis != null; lThis = lThis.getSuperclass())
        {
            lThis.getChildItems(MOwned.MY_CAT,ll);
        }
        for (Item lIt : ll)
        {
            if (!aOut.containsKey(lIt.getLID().getName()))
            {
                aOut.put(lIt.getLID().getName(), (MOwned) lIt);
            }
        }
    }

    public void findOwners(TreeMap<String, MOwner> aOut)
    {
        LinkedList<Item> ll = new LinkedList<Item>();
        for (MClass lThis = this; lThis != null; lThis = lThis.getSuperclass())
        {
            lThis.getChildItems(MOwned.MY_CAT,ll);
        }
        for (Item lIt : ll)
        {
            if (!aOut.containsKey(lIt.getLID().getName()))
            {
                aOut.put(lIt.getLID().getName(), ((MOwned) lIt).getOwner());
            }
        }
    }

    public Collection<MOwner> findOwners()
    {
        TreeMap<String, MOwner> lOwners = new TreeMap<String, MOwner>();
        findOwners(lOwners);
        return lOwners.values();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // NAMING APIs
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public MNamer findNamer()
    {
        MNamer lNamer = null;
        for (MClass lThis = this; null != lThis && null == lNamer; lThis = lThis.getSuperclass())
        {
            lNamer = MNamer.get(lThis.getGID().getName(), false);
        }
        return lNamer;
    }

    public void findNamingRules(Map<String, MNameRule> aOut)
    {
        for (MClass lThis = this; null != lThis; lThis = lThis.getSuperclass())
        {
            MNamer lNamer = MNamer.get(lThis.getGID().getName(), false);
            if (null != lNamer)
            {
                lNamer.getNamingRules(aOut);
            }
        }
    }

    public Collection<MNameRule> findNamingRules()
    {
        TreeMap<String,MNameRule> lR = new TreeMap<String, MNameRule>();
        findNamingRules(lR);
        return lR.values();
    }


    public MNameRule findNameRule(String aInParentClassGName)
    {
        MNameRule lRet = null;
        for (MNamer lThisNamer = findNamer();
             null == lRet && null != lThisNamer;
             lThisNamer = lThisNamer.getSuper())
        {
            lRet = lThisNamer.findNameRule(aInParentClassGName);
        }
        return lRet;
    }

    public Collection<List<MNameRule>> getNamingPaths()
    {
        return getNamingPaths(Language.CPP);
    }

    public Collection<List<MNameRule>> getNamingPaths(Language aInLangOrNull)
    {
        Collection<List<MNameRule>> lRet = new LinkedList<List<MNameRule>>();
        getNamingPaths(lRet, aInLangOrNull);
        return lRet;
    }

    public boolean getNamingPaths(Collection<List<MNameRule>> aOut, Language aInLangOrNull)
    {
        aInLangOrNull = null == aInLangOrNull ? Language.CPP : aInLangOrNull;

        TreeSet<String> lSignatues = new TreeSet<String>();
        boolean lUniqueSignatures = true;
        if (isConcrete() && !isRoot())
        {
            Collection<List<MClass>> lContPaths = getContainmentPaths();
            for (List<MClass> lContPath : lContPaths)
            {
                LinkedList<MNameRule> lNamePath = new LinkedList<MNameRule>();
                MClass lPrevClass = null;
                for (MClass lThisClass : lContPath)
                {
                    StringBuilder lPathSignature = new StringBuilder();

                    MNamer lNamer = lThisClass.findNamer();
                    if (null != lNamer)
                    {
                        MNameRule lNr = lThisClass.findNameRule(null == lPrevClass ? null : lPrevClass.getGID().getName());
                        if (null != lNr)
                        {
                            lNamePath.add(lNr);

                            Collection<MNameComponent> lNcs = lNr.getComponents();
                            for (MNameComponent lNc : lNcs)
                            {
                                if (lNc.hasPropName())
                                {
                                    lPathSignature.append(':');
                                    MProp lProp = lThisClass.findProp(lNc.getPropName(), true);
                                    if (null != lProp)
                                    {
                                        MType lType = lProp.getType(true);
                                        lPathSignature.append(lType.getLanguageBinding(aInLangOrNull).getSyntax());
                                    }
                                    else
                                    {
                                        Severity.DEATH.report(toString(), "naming path calculation", "",
                                                              "no prop" + lNc.getPropName() + " for: " + lThisClass.getLID().getName() + " ::: CONT PATHS: " + lContPaths);
                                    }
                                }
                            }

                        }
                        else
                        {
                            Severity.DEATH.report(toString(), "naming path calculation", "",
                                                  "no name for: " + lThisClass.getLID().getName() + " under " + lPrevClass + " ::: CONT PATHS: " + lContPaths);
                        }
                    }
                    else
                    {
                        Severity.DEATH.report(toString(), "naming path calculation", "",
                                              "no namer for: " + lThisClass.getGID().getName() + " ::: CONT PATHS: " + lContPaths);
                    }
                    lUniqueSignatures = lSignatues.add(lPathSignature.toString()) && lUniqueSignatures;

                    lPrevClass = lThisClass;
                }
                aOut.add(lNamePath);
            }
        }
        return lUniqueSignatures;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // IMPLICIT CALLBACK APIs
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void loadModelCompleteCb()
    {
        super.loadModelCompleteCb();
        initPropLocalIdx();
    }


    private final boolean isConcrete;
    private int numOfBaseProps = -1;
    private int numOfAllBaseProps = -1;
}

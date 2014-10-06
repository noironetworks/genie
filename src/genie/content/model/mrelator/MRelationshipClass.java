package genie.content.model.mrelator;

import genie.content.model.mclass.MClass;
import genie.content.model.module.Module;
import genie.engine.model.Cardinality;
import genie.engine.model.Relator;
import genie.engine.model.RelatorCat;
import modlan.report.Severity;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by midvorki on 10/2/14.
 */
public class MRelationshipClass extends MClass
{
    public static final RelatorCat SOURCE_CAT = RelatorCat.getCreate("mclass:reln-source", Cardinality.SINGLE);

    public MRelationshipClass(
            Module aInModule,
            String aInLName,
            MRelationship aInReln)
    {
        super(aInModule, aInLName, true);
        addSource(aInReln.getSourceClassGName());
        reln.add(aInReln);
    }

    public void addTargetRelationship(MRelationship aIn)
    {
        reln.add(aIn);
    }

    public Collection<MRelationship> getRelationships()
    {
        return reln;
    }

    public MClass getSourceClass()
    {
        Relator lRel = SOURCE_CAT.getRelator(getGID().getName());
        if (null == lRel)
        {
            Severity.DEATH.report("relationshop class", "retrieval of source class", "source class not found", "no relationship registered");
        }
        MClass lClass = (MClass) (null == lRel ? null : lRel.getToItem());
        if (null == lClass)
        {
            Severity.DEATH.report("relationshop class", "retrieval of source class", "source class not found", "bad relationship");
        }
        return lClass;
    }

    public Collection<MClass> getTargetClasses()
    {
        LinkedList<MClass> lRet = new LinkedList<MClass>();
        for (MRelationship lRel : reln)
        {
            lRet.add(lRel.getTargetClass());
        }
        return lRet;
    }

    private void addSource(String aInClassGName)
    {
        SOURCE_CAT.add(MY_CAT, getGID().getName(), MY_CAT, aInClassGName);
    }



    private Collection<MRelationship> reln = new LinkedList<MRelationship>();
}
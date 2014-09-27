package genie.content.parse.pnaming;

import genie.content.model.mclass.DefinitionScope;
import genie.content.model.mnaming.MNamer;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.model.ProcessorNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 8/1/14.
 */
public class PNamedNode extends ParseNode
{
    public PNamedNode(String aInName)
    {
        super(aInName);
    }

    protected void addParent(ProcessorNode aInParent)
    {
        super.addParent(aInParent);
        scope = DefinitionScope.get(aInParent.getName());
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {
        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE,
                                              MNamer.get(
                                                      (DefinitionScope.CLASS == scope ?
                                                              aInParentItem.getGID().getName() :
                                                              aInData.getNamedValue(Strings.CLASS,
                                                                                    aInData.getNamedValue(Strings.NAME, null, false),
                                                                                    true)),
                                                      true));
    }

    private DefinitionScope scope = DefinitionScope.NONE;

}
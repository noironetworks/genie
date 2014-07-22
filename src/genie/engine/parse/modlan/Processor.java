package genie.engine.parse.modlan;

import genie.engine.model.Item;
import genie.engine.model.Pair;

import java.util.Collection;

/**
 * Created by midvorki on 3/22/14.
 */
public interface Processor
{
    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItemOrNull);
    public void endCB(Node aInData, Item aInItemOrNull);
    public Processor getChild(String aInName);
    public boolean hasProp(String aInName);
    public Collection<String> getPropNames();
}

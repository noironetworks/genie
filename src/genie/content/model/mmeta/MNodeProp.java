package genie.content.model.mmeta;

import genie.engine.model.Cat;
import genie.engine.model.Item;
import genie.engine.parse.model.ParseNodePropType;

/**
 * Created by midvorki on 7/22/14.
 */
public class MNodeProp
        extends Item
{
    /**
     * category of this item
     */
    public static final Cat MY_CAT = Cat.getCreate("parser:meta:prop");

    public MNodeProp(MNode aInParent, String aInName, ParseNodePropType aInType)
    {
        super(MY_CAT, aInParent, aInName);
        type = aInType;
    }

    public ParseNodePropType getType()
    {
        return type;
    }

    private ParseNodePropType type;
}

package genie.content.model.mtype;

import genie.engine.model.Cat;

/**
 * Created by dvorkinista on 7/7/14.
 */
public class MTypeHint
        extends SubTypeItem
{
    public static final Cat MY_CAT = Cat.getCreate("type:hint");
    public static final String NAME = "hint";

    public MTypeHint(
            MType aInType
                    )
    {
        super(MY_CAT, aInType, NAME);
    }

    // TODO: NEED TO IMPLEMENT TYPE HINT
}
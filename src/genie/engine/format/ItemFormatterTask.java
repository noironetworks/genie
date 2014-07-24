package genie.engine.format;

import genie.engine.file.WriteStats;
import genie.engine.model.Item;

/**
 * Created by midvorki on 7/24/14.
 */
public abstract class ItemFormatterTask extends FormatterTask
{
    protected ItemFormatterTask(Formatter aInFormatter, Item aInItem)
    {
        super(aInFormatter);
        item = aInItem;
    }

    protected ItemFormatterTask(FormatterCtx aInFormatterCtx,
            FileNameRule aInFileNameRule,
            Indenter aInIndenter,
            BlockFormatDirective aInHeaderFormatDirective,
            BlockFormatDirective aInCommentFormatDirective,
            String aInName,
            boolean aInIsUserFile,
            WriteStats aInStats,
            Item aInItem)
    {
        super(aInFormatterCtx,
              aInFileNameRule,
              aInIndenter,
              aInHeaderFormatDirective,
              aInCommentFormatDirective,
              aInName,
              aInIsUserFile,
              aInStats);

        item = aInItem;
    }

    public Item getItem()
    {
        return item;
    }

    private final Item item;
}


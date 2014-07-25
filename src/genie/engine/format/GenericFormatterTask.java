package genie.engine.format;

import genie.engine.file.WriteStats;
import genie.engine.model.Item;

/**
 * Created by midvorki on 7/25/14.
 */
public abstract class GenericFormatterTask extends FormatterTask
{
    protected GenericFormatterTask(Formatter aInFormatter)
    {
        super(aInFormatter);
    }

    protected GenericFormatterTask(FormatterCtx aInFormatterCtx,
                                   FileNameRule aInFileNameRule,
                                   Indenter aInIndenter,
                                   BlockFormatDirective aInHeaderFormatDirective,
                                   BlockFormatDirective aInCommentFormatDirective,
                                   String aInName,
                                   boolean aInIsUserFile,
                                   WriteStats aInStats)
    {
        super(aInFormatterCtx,
              aInFileNameRule,
              aInIndenter,
              aInHeaderFormatDirective,
              aInCommentFormatDirective,
              aInName,
              aInIsUserFile,
              aInStats);
    }
}
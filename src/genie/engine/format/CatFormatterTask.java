package genie.engine.format;

import genie.engine.file.WriteStats;
import genie.engine.model.Cat;

/**
 * Created by midvorki on 7/24/14.
 */
public abstract class CatFormatterTask extends FormatterTask
{
    protected CatFormatterTask(Formatter aInFormatter, Cat aInCat)
    {
        super(aInFormatter);
        cat = aInCat;
    }

    protected CatFormatterTask(FormatterCtx aInFormatterCtx,
                               FileNameRule aInFileNameRule,
                               Indenter aInIndenter,
                               BlockFormatDirective aInHeaderFormatDirective,
                               BlockFormatDirective aInCommentFormatDirective,
                               String aInName,
                               boolean aInIsUserFile,
                               WriteStats aInStats,
                               Cat aInCat)
    {
        super(aInFormatterCtx,
              aInFileNameRule,
              aInIndenter,
              aInHeaderFormatDirective,
              aInCommentFormatDirective,
              aInName,
              aInIsUserFile,
              aInStats);

        cat = aInCat;
    }

    public Cat getCat()
    {
        return cat;
    }

    private final Cat cat;
}
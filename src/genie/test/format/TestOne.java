package genie.test.format;

import genie.engine.file.WriteStats;
import genie.engine.format.*;

/**
 * Created by midvorki on 7/25/14.
 */
public class TestOne extends GenericFormatterTask
{
    public TestOne(FormatterCtx aInFormatterCtx,
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

    public void generate()
    {
        System.out.println(this + ".generate()");
        out.println("Hi, I really really like you");
    }
}

package genie.content.format.build.automake.cpp;

import genie.content.model.mmeta.MNode;
import genie.engine.file.WriteStats;
import genie.engine.format.*;

/**
 * Created by midvorki on 10/6/14.
 */
public class FAutomakeDef
        extends GenericFormatterTask
{
    public FAutomakeDef(
            FormatterCtx aInFormatterCtx, FileNameRule aInFileNameRule, Indenter aInIndenter, BlockFormatDirective aInHeaderFormatDirective, BlockFormatDirective aInCommentFormatDirective, String aInName, boolean aInIsUserFile, WriteStats aInStats
                   )
    {
        super(aInFormatterCtx, aInFileNameRule, aInIndenter, aInHeaderFormatDirective, aInCommentFormatDirective, aInName, aInIsUserFile, aInStats);
    }

    public void generate()
    {
    }
}
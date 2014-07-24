package genie.engine.format;

import genie.engine.file.WriteStats;
import genie.engine.proc.Task;

/**
 * Created by midvorki on 7/24/14.
 */
public abstract class FormatterTask implements Task
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // INTERNAL APIs
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * formatter writer. use like: out.println("it's a sunny day");
     */
    protected Formatter out = null;

    /**
     * file name formatter. formats the name excluding prefix and suffix.
     * this methid is overridable by subclasses for customization
     * @return formatted name excluding prefix and suffix
     */
    protected String formatFileName()
    {
        return name;
    }

    /**
     * description formatter
     * @return description
     */
    abstract protected String[] formatDescription();
    /**
     * CODE GENERATION HANDLE.
     */
    public abstract void generate();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // INTERNAL APIs
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected FormatterTask(Formatter aInFormatter)
    {
        this(
          aInFormatter.getFormattedFile().getCtx(),
          aInFormatter.getFormattedFile().getFileNameRule(),
          aInFormatter.getIndenter(),
          aInFormatter.getHeaderFormatDirective(),
          aInFormatter.getCommentFormatDirective(),
          aInFormatter.getFormattedFile().getFileName(),
          !aInFormatter.getFormattedFile().isOverrideExisting(),
          aInFormatter.getFormattedFile().getStats()
          );
        file = aInFormatter.getFormattedFile();
        out = aInFormatter;
    }

    protected FormatterTask(
            FormatterCtx aInFormatterCtx,
            FileNameRule aInFileNameRule,
            Indenter aInIndenter,
            BlockFormatDirective aInHeaderFormatDirective,
            BlockFormatDirective aInCommentFormatDirective,
            String aInName,
            boolean aInIsUserFile,
            WriteStats aInStats)
    {
        formatterCtx = aInFormatterCtx;
        fileNameRule = aInFileNameRule;
        indenter = aInIndenter;
        headerFormatDirective = aInHeaderFormatDirective;
        commentFormatDirective = aInCommentFormatDirective;
        name = aInName;
        isUserFile = aInIsUserFile;
        stats = aInStats;
    }

    public FormatterCtx getFormatterCtx() { return formatterCtx; }
    public FileNameRule getFileNameRule() { return fileNameRule; }
    public Indenter getIndenter() { return indenter; }
    public BlockFormatDirective getHeaderFormatDirective() { return headerFormatDirective; }
    public BlockFormatDirective getCommentFormatDirective() { return commentFormatDirective; }
    public String getName() { return name; }
    public boolean isUserFile() { return isUserFile; }
    public WriteStats getStats() { return stats; }

    public final void run()
    {
        init();
        generate();
        finish();
    }

    protected void init()
    {
        if (null == file)
        {
            file = new FormattedFile(
                    formatterCtx,
                    fileNameRule,
                    formatFileName(),
                    !isUserFile,
                    stats);
        }
        if (null == out)
        {
            out = new Formatter(
                    file,
                    indenter,
                    headerFormatDirective,
                    commentFormatDirective,
                    formatDescription());
        }
    }

    protected void finish()
    {
        out.flush();
        out.close();
    }

    private FormattedFile file = null;

    private final FormatterCtx formatterCtx;
    private final FileNameRule fileNameRule;
    private final Indenter indenter;
    private final BlockFormatDirective headerFormatDirective;
    private final BlockFormatDirective commentFormatDirective;
    private final String name;
    private final boolean isUserFile;
    private final WriteStats stats;
}

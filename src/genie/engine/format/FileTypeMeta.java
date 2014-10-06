package genie.engine.format;

import modlan.report.Severity;

/**
 * Created by midvorki on 7/24/14.
 */
public enum FileTypeMeta
{
    C_HEADER("c:header",
             ".h",
             new Indenter(10, 4, true),
             new BlockFormatDirective(
                    "/**",
                    "* ",
                    null,
                    "*/",
                    0),
             new BlockFormatDirective(
                     "/*",
                     "* ",
                     null,
                     "*/",
                     0)
             ),
    C_SOURCE("c:source",
             ".c",
             new Indenter(10, 4, true),
             new BlockFormatDirective(
                     "/**",
                     "* ",
                     null,
                     "*/",
                     0),
             new BlockFormatDirective(
                     "/*",
                     "*",
                     null,
                     "*/",
                     0)),
    CPP_HEADER("cpp:header",
             ".hpp",
             new Indenter(10, 4, true),
             new BlockFormatDirective(
                     "/**",
                     "* ",
                     null,
                     "*/",
                     0),
             new BlockFormatDirective(
                     "/*",
                     "* ",
                     null,
                     "*/",
                     0)
    ),
    CPP_SOURCE("cpp:source",
             ".cpp",
             new Indenter(10, 4, true),
             new BlockFormatDirective(
                     "/**",
                     "* ",
                     null,
                     "*/",
                     0),
             new BlockFormatDirective(
                     "/*",
                     "*",
                     null,
                     "*/",
                     0)),
    MDL_META("meta",
              ".meta",
              new Indenter(10, 4, true),
              new BlockFormatDirective(
                      "# ",
                      "# ",
                      null,
                      "# ",
                      0),
              new BlockFormatDirective(
                      "# ",
                      "# ",
                      null,
                      "# ",
                      0)),
    AUTOMAKE("automake",
             ".am",
             new Indenter(true, 10, 1, true),
             new BlockFormatDirective(
                     "# ",
                     "# ",
                     null,
                     "# ",
                     0),
             new BlockFormatDirective(
                     "# ",
                     "# ",
                     null,
                     "# ",
                     0)),
    ;
    private FileTypeMeta(String aInName,
                         String ainFileExt,
                         Indenter aInIndenter,
                         BlockFormatDirective aInHeaderFormatDirective,
                         BlockFormatDirective aInCommentFormatDirective)
    {
        name = aInName;
        fileExt = ainFileExt;
        indenter = aInIndenter;
        headerFormatDirective = aInHeaderFormatDirective;
        commentFormatDirective = aInCommentFormatDirective;
    }

    public String getName() { return name; }
    public String getFileExt() { return fileExt; }
    public Indenter getIndenter() { return indenter; }
    public BlockFormatDirective getHeaderFormatDirective() { return headerFormatDirective; }
    public BlockFormatDirective getCommentFormatDirective() { return commentFormatDirective; }

    public static FileTypeMeta get(String aIn)
    {
        for (FileTypeMeta lThis : FileTypeMeta.values())
        {
            if (lThis.name.equalsIgnoreCase(aIn))
            {
                return lThis;
            }
        }
        Severity.DEATH.report(
                "FileTypeMeta",
                "get file type meta for name",
                "no such file type meta",
                "no support for " + aIn);
        return null;
    }

    public String toString()
    {
        return "file-type-meta(" + name + ')';
    }
    private final String name;
    private final String fileExt;
    private final Indenter indenter;
    private final BlockFormatDirective headerFormatDirective;
    private final BlockFormatDirective commentFormatDirective;
}

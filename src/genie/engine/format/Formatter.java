package genie.engine.format;

import modlan.utils.Strings;

/**
 * Created by midvorki on 7/23/14.
 */
public abstract class Formatter
{
    public Formatter(
            FormattedFile aInFormattedFile, Indenter aInIndenter, BlockFormatDirective aInHeaderFormatDirective, BlockFormatDirective aInCommentFormatDirective, String[] aInDescription
                    )
    {
        formattedFile = aInFormattedFile;
        indenter = aInIndenter;
        headerFormatDirective = aInHeaderFormatDirective;
        commentFormatDirective = aInCommentFormatDirective;
        if (null != aInHeaderFormatDirective)
        {
            printHeaderComment(
                    -1,
                    getHeaderComments(aInDescription,
                                      aInFormattedFile.isOverrideExisting()));
        }
    }

    protected String[] getHeaderComments(
            String[] aInDescription,
            boolean aInDoNotOverwriteExisting)
    {
        String[] lStr = new String[6 + aInDescription.length];
        int i = 0;
        lStr[i++] = "SOME COPYRIGHT"; // TODO: insieme.ngen.NGen.getCopyrightMessage(),
        lStr[i++] = " ";
        lStr[i++] = formattedFile.getFullFileName();
        lStr[i++] = " ";
        lStr[i++] = "generated " + formattedFile.getFullFileName() +
                  " file genie code generation framework free of license." +
                  (aInDoNotOverwriteExisting ? "with manual implementation detail." : "");
        lStr[i++] = " ";
        for (String lThatLine : aInDescription)
        {
            lStr[i++] = lThatLine;
        }
        return lStr;
    }

    public void flush()
    {
        formattedFile.flush();
    }

    public void close()
    {
        formattedFile.close();
    }

    public String getIndent(int aInDepth)
    {
        return indenter.get(aInDepth);
    }

    public void printHeaderComment(
            int aInIndent,
            String aInComment)
    {
        printHeaderComment(aInIndent, new String[]{aInComment});
    }

    public void printHeaderComment(
            int aInIndent,
            String[] aInComment)
    {
        printComment(aInIndent, aInComment, headerFormatDirective);
    }

    public void printIncodeComment(
            int aInIndent,
            String aInComment)
    {
        printIncodeComment(aInIndent, new String[]{aInComment});
    }

    public void printIncodeComment(
            int aInIndent,
            String[] aInComment)
    {
        printComment(
                aInIndent,
                aInComment,
                commentFormatDirective);
    }

    public void printComment(
            int aInIndent,
            String[] aInComment,
            BlockFormatDirective aInFormatDirs)
    {
        if (null != aInComment &&
            0 < aInComment.length)
        {

            if (aInFormatDirs.hasOpenerString())
            {
                println(aInIndent, aInFormatDirs.getOpenerString());
            }
            for (String lThisComment : aInComment)
            {
                if (!Strings.isEmpty(lThisComment))
                {
                    if (aInFormatDirs.hasLineStartString())
                    {
                        print(aInIndent + aInFormatDirs.getLineIndentOffset(), aInFormatDirs.getLineStartString());
                    }

                    print(lThisComment);

                    if (aInFormatDirs.hasLineEndString())
                    {
                        println(aInFormatDirs.getLineEndString());
                    }
                    else
                    {
                        println();
                    }
                }
            }
            if (aInFormatDirs.hasTerminatorString())
            {
                println(aInIndent, aInFormatDirs.getTerminatorString());
            }
        }
    }

    public void print()
    {

    }

    public void indent(int aInIndentDepth)
    {
        if (-1 < aInIndentDepth)
        {
            formattedFile.print(getIndent(aInIndentDepth));
        }
    }

    public void print(boolean aInValue)
    {
        formattedFile.print(aInValue);
    }

    public void print(int aInIndentDepth, boolean aInValue)
    {
        indent(aInIndentDepth);
        print(aInValue);
    }

    public void print(char aInValue)
    {
        formattedFile.print(aInValue);
    }

    public void print(int aInIndentDepth, char aInValue)
    {
        indent(aInIndentDepth);
        print(aInValue);
    }

    public void print(char[] aInValue)
    {
        formattedFile.print(aInValue);
    }

    public void print(int aInIndentDepth, char[] aInValue)
    {
        indent(aInIndentDepth);
        print(aInValue);
    }

    public void print(double aInValue)
    {
        formattedFile.print(aInValue);
    }

    public void print(int aInIndentDepth, double aInValue)
    {
        indent(aInIndentDepth);
        print(aInValue);
    }

    public void print(float aInValue)
    {
        formattedFile.print(aInValue);
    }

    public void print(int aInIndentDepth, float aInValue)
    {
        indent(aInIndentDepth);
        print(aInValue);
    }

    public void print(int aInValue)
    {
        formattedFile.print(aInValue);
    }

    public void print(int aInIndentDepth, int aInValue)
    {
        indent(aInIndentDepth);
        print(aInValue);
    }

    public void print(long aInValue)
    {
        formattedFile.print(aInValue);
    }

    public void print(int aInIndentDepth, long aInValue)
    {
        indent(aInIndentDepth);
        print(aInValue);
    }

    public void print(Object aInValue)
    {
        formattedFile.print(aInValue);
    }

    public void print(int aInIndentDepth, Object aInValue)
    {
        indent(aInIndentDepth);
        print(aInValue);
    }

    public void print(String aInValue)
    {
        formattedFile.print(aInValue);
    }

    public void print(int aInIndentDepth, String aInValue)
    {
        indent(aInIndentDepth);
        print(aInValue);
    }

    public void print(int aInIndentDepth, String format, Object... args)
    {
        indent(aInIndentDepth);
        formattedFile.print(format, args);
    }

    public void println()
    {
        formattedFile.println();
    }

    public void indentln(int aInIndentDepth)
    {
        indent(aInIndentDepth);
        println();
    }

    public void println(boolean aInValue)
    {
        formattedFile.println(aInValue);
    }

    public void println(int aInIndentDepth, boolean aInValue)
    {
        indent(aInIndentDepth);
        println(aInValue);
    }

    public void println(char aInValue)
    {
        formattedFile.println(aInValue);
    }

    public void println(int aInIndentDepth, char aInValue)
    {
        indent(aInIndentDepth);
        println(aInValue);
    }

    public void println(char[] aInValue)
    {
        formattedFile.println(aInValue);
    }

    public void println(int aInIndentDepth, char[] aInValue)
    {
        indent(aInIndentDepth);
        println(aInValue);
    }

    public void println(double aInValue)
    {
        formattedFile.println(aInValue);
    }

    public void println(int aInIndentDepth, double aInValue)
    {
        indent(aInIndentDepth);
        println(aInValue);
    }

    public void println(float aInValue)
    {
        formattedFile.println(aInValue);
    }

    public void println(int aInIndentDepth, float aInValue)
    {
        indent(aInIndentDepth);
        println(aInValue);
    }

    public void println(int aInValue)
    {
        formattedFile.println(aInValue);
    }

    public void println(int aInIndentDepth, int aInValue)
    {
        indent(aInIndentDepth);
        println(aInValue);
    }

    public void println(long aInValue)
    {
        formattedFile.println(aInValue);
    }

    public void println(int aInIndentDepth, long aInValue)
    {
        indent(aInIndentDepth);
        println(aInValue);
    }

    public void println(Object aInValue)
    {
        formattedFile.println(aInValue);
    }

    public void println(int aInIndentDepth, Object aInValue)
    {
        indent(aInIndentDepth);
        println(aInValue);
    }

    public void println(String aInValue)
    {
        formattedFile.println(aInValue);
    }

    public void println(int aInIndentDepth, String aInValue)
    {
        indent(aInIndentDepth);
        println(aInValue);
    }

    public void println(String format, Object... args)
    {
        formattedFile.println(format, args);
    }

    public void println(int aInIndentDepth, String format, Object... args)
    {
        indent(aInIndentDepth);
        formattedFile.println(format, args);
    }

    /**
     * CODE GENERATION HANDLE.
     */
    public abstract void generate();

    public void execute()
    {
        generate();
        flush();
        close();
    }

    private final FormattedFile formattedFile;
    private final Indenter indenter;
    private final BlockFormatDirective headerFormatDirective;
    private final BlockFormatDirective commentFormatDirective;
}

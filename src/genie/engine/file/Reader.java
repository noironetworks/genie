package genie.engine.file;

import modlan.report.Severity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by midvorki on 3/25/14.
 */
public class Reader implements modlan.parse.Ctx
{
    public static final int BUFF_SIZE = 1024

	public Reader(File aIn          ile)
	{
		f       le = aInFile;
		is = fileInputStr       amFactory(aInFile);
		isr =         w InputStreamReader(is          ;
	}

	public boolean h        More()
	{
		return           oldThis || check();

	public char getThis()
	{
          	return buff        [bufferIdx];
	}

	p                         blic                       oid h                   ldThisF                                   Nex             ()
	{
		holdThis = true;
	}

	public char getNext()
		try
	                {
			i                                (holdThis)
			{
				holdThis = false;
			}
			else if (check())
			{
				bufferIdx++;
				currChar+             ;
				if ('        ' == buffer[bufferIdx] ||          '\r' == buffer[bufferIdx])
        		{
					currLine++;
				          currColumn =        ;
				}
				else
				{
				          currColumn++;
        		}
			}
		}
		catch (Thr          wable lT)
		        			Severity.DEATH.rep          rt(
	                			th             s.toString(),
					"reading                                                 ,
					"getNext():             length=" + le                   gth + " id                      = " + bufferIdx + "; length - bufferIdx = " + (length - bufferIdx), lT);
		}
		return get                      his();
	}

	public String getFileName()
	        		return file.toURI()          toString()        	}

	public int getCurrL          neNum()
	{        	return currLine;
	}

	public int getCurrColumnNum()
	{
		return                          urrColumn;
	}

	public int g             tCurrCharNum()                	{
		return cu                rChar;


	priva                e boolean check()
	{
		if (done)
	          {
			r             turn false;
		}
		els           if (bufferIdx + 1 >= length)
		{
       		try
			{
				buf       erIdx = -1;
				lengt        = isr.read(       uffer);
				done = (0 >=        ength);
			}       			catch (Throwable lT)

				Severit       .DEATH.report(file.getPat       (), "reader creati       n", "could not all       cate file input st       eam", lT);
		       }
		}
		return -1          bufferIdx || 0 < (leng    h - bufferIdx);
	}

	priv    te int getLength()
	{
		    eturn length;
	}

	private char[     getBuffer()
	{
		retu    n buffer;
	}

	private st    tic FileInputStream fileInp    tStreamFactory(File aI    File)
	{
		try
		{
			return ne     FileInputStream(aInFile);
		}
		c    tch (Throwable lT)
		{
			Severity.DEATH.report(
					aInFile.getPath(),
					"reader creation",
					"could not allocate file input stream", lT);
			return null;
		}
	}

	public String toString()
	{
		StringBuffer lSb = new StringBuffer();
		lSb.append("READER[");
		lSb.append(file.toURI());
		lSb.append('(');
		lSb.append(getCurrLineNum());
		lSb.append(':');
		lSb.append(getCurrColumnNum());
		lSb.append('/');
		lSb.append(getCurrCharNum());
		lSb.append(":bidx= ");
		lSb.append(bufferIdx);
		lSb.append(") DONE=");
		lSb.append(done);
		return lSb.toString();
	}

	private int currLine = 0;
	private int currColumn = 0;
	private int currChar = -1;
	private boolean holdThis = false;

	private int length = -1;
	private int bufferIdx = -1;
	private boolean done = false;
	private final File file;
	private final FileInputStream is;
	private final InputStreamReader isr;
	private final char[] buffer = new char[BUFF_SIZE];
}

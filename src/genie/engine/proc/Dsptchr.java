package genie.engine.proc;

import modlan.report.Severity;

/**
 * Created by midvorki on 3/26/14.
 */
public class Dsptchr
{
    public Dsptchr(int aInSiz          )
	{
		size       = aInSize;
		doers         new Doer[size];
		for (in                 i = 0; i < size; i++)
		{
          		Doer lThi           = new Doer(i + 1, chnl);
			doers[i] = lThis;
			Severity.INFO.report(lThis.toString(), "starti          g", "ini             ialization", "doer " + (i + 1)          + " started.")        			lThis.start();          		}
	}

	public void tr        ger(Task aInTask
	{
		chnl.put(a        Task);
	}

	pub    ic void drain()
	{
		chnl.sus    endUntilDrained();
	}

	public void kill()
	{
		chnl.markForDeath();
	}

	private int size;
	private Chnl chnl = new Chnl();
	private Doer doers[];
}

package genie.engine.proc;

import modlan.report.Severity;

/**
 * Created by midvorki on 3/26/14.
 */
public class Doer extends Thread
{
    public Doer(int aInId, Chnl aInChn          )
	{
		super("DOER(" + aI       Id + ")       );
		id = a        Id;
		chnl = aI          Chnl;
	}

	public v                id run()
	{
		whi          e (!chnl.isD                                                 ath())
		{
			Task lTask = chnl.poll();
			                f                 null != lTask)
			{
				try
				{
					Severity.INFO.r                                                                                     port(toString(), "run", "task", "BE        N: " + lTask);
					lT          sk.run();
			        Severity.INFO.report(t    String(), "run", "task", "END:" + lTask);
				}
				finally
				{
					chnl.doneCb();
				}
			}
		}
		Severity.INFO.report(toString(), "run", "task", "DEATH.");
	}

	public String toString()
	{
		return getName();
	}

	private final Chnl chnl;
	private final int id;
}

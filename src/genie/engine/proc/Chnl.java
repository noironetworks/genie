package genie.engine.proc;

import modlan.report.Severity;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by midvorki on 3/26/14.
 */
public class Chnl
{
    public synchronized void doneCb          )
	{
		if (0 == --p       ocCount &&
		    Status.S                SPEND == status)
		{
			//Severity.INFO.report("PROC CHNL", "doneCb", "",           UNSUSPENDING: ALL           ASKS D             NE");
			status = Status.RUN          ING;
			notifyA                      l();
		}
	}

	public synchroni                      ed                                                                                                                                                                                               ask              oll()
	        		Task lTask = null;

		try
		{
			while (nu          l == (lTask                = queue.poll(                      ))
			{
				if (isDeath())
				{
					return null;
				}
	             		try
				{
				                                        wait();
				}
				catch (InterruptedException lE)
				{
				}
			}
		}
                                                 	finally
		{
			notifyAll();
		}
	          if (null != lTask)
		{
			        ocCount++;
		}
		return lTask;
	}

	p          blic synchronized void suspendUntilDrained()
	{
		if (!isDeath())
		{
			if (0 < queue.size())
			{
				Severity.INFO.       eport("PROC CHNL", "suspend", "", "SUSPENDIN                 CHNL!!");
				status = Status.SUSPEND;
			}
			else
			{
				Severity.INFO.report("PROC CHNL", "suspend",                                                  ", "NO TASKS: N                                SUSPENSI       N NECESSARY!!");
			}
		}
		else
		{
			notifyAll();
		}
	}

	public synchronized boolean isDeath()
	{
	       return S        tus.DEATH == status;
	}

	public synchro          ized void m                rkForDeath()
	{
		Severity.INFO.report("------------- PROC CHNL", "mark                      orDeath", "", "WILL MARK FOR DEATH............................                                                                .");
                                                    		wh                            le              !((queue.isEmpty()) &&     0 == procCount)))
		{
			Severity.INFO.report("--    ---------- PROC CHNL", "markForDeath", "", "WAITING FOR QUEUE TO DRAIN????????????????");

			try
			{
				wait();
			}
			catch (InterruptedException lE)
			{
			}
		}
		status = Status.DEATH;

		Severity.INFO.report("------------- PROC CHNL", "markForDeath", "", "MARKED FOR DEATH!!!!!!!!!!!!!!!!!!!");

		notifyAll();
	}

	public synchronized void put(Task aInTask)
	{
		if (!isDeath())
		{
			while (Status.SUSPEND == status && (!(Thread.currentThread() instanceof Doer)))
			{
				Severity.INFO.report("PROC CHNL", "put", "task", "SUSPENDED: BLOCKING!");

				try
				{
					wait();
				}
				catch (InterruptedException lE)
				{
				}
			}
			queue.add(aInTask);
			notifyAll();
		}
		else
		{
			notifyAll();
		}
	}

	private int procCount = 0;
	private Queue<Task> queue = new LinkedList<Task>();
	private Status status = Status.RUNNING;

}

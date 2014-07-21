package genie.engine.proc;

import modlan.report.Severity;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by midvorki on 3/26/14.
 */
public class Chnl
{
	public synchronized void doneCb()
	{
		if (0 == --procCount &&
		    Status.SUSPEND == status)
		{
			//Severity.INFO.report("PROC CHNL", "doneCb", "", "UNSUSPENDING: ALL TASKS DONE");
			status = Status.RUNNING;
			notifyAll();
		}
	}

	public synchronized Task poll()
	{
		Task lTask = null;

		try
		{
			while (null == (lTask = queue.poll()))
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
			procCount++;
		}
		return lTask;
	}

	public synchronized void suspendUntilDrained()
	{
		if (!isDeath())
		{
			if (0 < queue.size())
			{
				Severity.INFO.report("PROC CHNL", "suspend", "", "SUSPENDING CHNL!!");
				status = Status.SUSPEND;
			}
			else
			{
				Severity.INFO.report("PROC CHNL", "suspend", "", "NO TASKS: NO SUSPENSION NECESSARY!!");
			}
		}
		else
		{
			notifyAll();
		}
	}

	public synchronized boolean isDeath()
	{
		return Status.DEATH == status;
	}

	public synchronized void markForDeath()
	{
		Severity.INFO.report("------------- PROC CHNL", "markForDeath", "", "WILL MARK FOR DEATH..............................");

		while (!((queue.isEmpty()) && (0 == procCount)))
		{
			Severity.INFO.report("------------- PROC CHNL", "markForDeath", "", "WAITING FOR QUEUE TO DRAIN????????????????");

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

    public synchronized void waitOutSuspense()
    {
        if (!isDeath())
        {
            while (Status.SUSPEND == status)
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
            notifyAll();
        }
        else
        {
            notifyAll();
        }
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

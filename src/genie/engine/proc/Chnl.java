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
			//Severity.INFO.report("processor:chnl", "doneCb", "", "UNSUSPENDING: ALL TASKS DONE");
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
			if (hasOutstandingTasks())
			{
				Severity.INFO.report("processor:chnl", "suspend", "", "SUSPENDING CHNL!!");
				status = Status.SUSPEND;
			}
			else
			{
				Severity.INFO.report("processor:chnl", "suspend", "", "NO TASKS: NO SUSPENSION NECESSARY!!");
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

    private boolean hasOutstandingTasks()
    {
        return !((queue.isEmpty()) && (0 == procCount));
    }
	public synchronized void markForDeath()
	{
		Severity.INFO.report("processor:chnl", "markForDeath", "", "WILL MARK FOR DEATH..............................");

		while (hasOutstandingTasks())
		{
			Severity.INFO.report("processor:chnl", "markForDeath", "", "WAITING FOR QUEUE TO DRAIN????????????????");

			try
			{
				wait();
			}
			catch (InterruptedException lE)
			{
			}
		}
		status = Status.DEATH;

		Severity.INFO.report("processor:chnl", "markForDeath", "", "MARKED FOR DEATH!!!!!!!!!!!!!!!!!!!");

		notifyAll();
	}

    public synchronized void waitOutSuspense()
    {
        while (Status.SUSPEND == status && hasOutstandingTasks()) //&& 0 < queue.size())
        {
            Severity.INFO.report("processor:chnl", "put", "task", "SUSPENDED: WAITING OUT THE SUSPENSE!");

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

	public synchronized void put(Task aInTask)
	{
		if (!isDeath())
		{
			while (Status.SUSPEND == status && (!(Thread.currentThread() instanceof Doer)))
			{
				Severity.INFO.report("processor:chnl", "put", "task", "SUSPENDED: BLOCKING!");

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

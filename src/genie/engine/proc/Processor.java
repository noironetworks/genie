package genie.engine.proc;

import genie.content.parse.modlan.ParseRegistry;
import genie.engine.parse.model.ProcessorTree;

/**
 * Created by midvorki on 4/4/14.
 */
public class Processor
{
	public Processor(
			int aInParallelism,
			String aInModelPreLoadPathsInOrder[][],
			String aInModelLoadPathsInOrder[][],
			ProcessorTree aInPTree,
	        String aInDestPath
	        )
	{
		dsp = new Dsptchr(aInParallelism);
		pTree = aInPTree;
		modelPreLoadPaths = aInModelPreLoadPathsInOrder;
		modelLoadPaths = aInModelLoadPathsInOrder;

		loadTargets = new LoadTarget[modelPreLoadPaths.length + modelLoadPaths.length];

		process();
	}

	private void process()
	{
		load();
		dsp.drain();
		dsp.kill();
	}

	private void load()
	{
		int i, j;
		// FIRST PRE-LOAD SENSITIVE STUFF
		for (i = 0; i < modelPreLoadPaths.length; i++)
		{
			loadTargets[i] = new LoadTarget(dsp,pTree,new String[]{ modelPreLoadPaths[i][0]}, modelPreLoadPaths[i][1]);
			dsp.drain();
		}
		// NOW LOAD GENERAL MODELS
		for (j = 0; j < modelPreLoadPaths.length; j++)
		{
			loadTargets[i++] = new LoadTarget(dsp,pTree,new String[]{ modelPreLoadPaths[j][0]}, modelPreLoadPaths[j][1]);
		}
	}
	private final String modelPreLoadPaths[][];
	private final String modelLoadPaths[][];
	private final LoadTarget loadTargets[];
	private final ProcessorTree pTree;
	private final Dsptchr dsp;
}

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
            String aInMetadataLoadPathsInOrder[][],
			String aInModelPreLoadPathsInOrder[][],
			String aInModelLoadPathsInOrder[][],
			ProcessorTree aInPTree,
	        String aInDestPath
	        )
	{
		dsp = new Dsptchr(aInParallelism);
		pTree = aInPTree;
        metadataLoadPaths = aInMetadataLoadPathsInOrder;
        modelPreLoadPaths = aInModelPreLoadPathsInOrder;
		modelLoadPaths = aInModelLoadPathsInOrder;

		loadTargets = new LoadTarget[metadataLoadPaths.length + modelPreLoadPaths.length + modelLoadPaths.length];

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
		int i, j, m;
        // FIRST PRE-LOAD SENSITIVE STUFF
        for (m = 0; m < metadataLoadPaths.length; m++)
        {
            loadTargets[m] = new LoadTarget(dsp,pTree,new String[]{ metadataLoadPaths[m][0]}, metadataLoadPaths[m][1]);
            dsp.drain();
        }
		// FIRST PRE-LOAD SENSITIVE STUFF
		for (i = 0; i < modelPreLoadPaths.length; i++)
		{
			loadTargets[i] = new LoadTarget(dsp,pTree,new String[]{ modelPreLoadPaths[i][0]}, modelPreLoadPaths[i][1]);
			dsp.drain();
		}
		// NOW LOAD GENERAL MODELS
		for (j = 0; j < modelLoadPaths.length; j++)
		{
			loadTargets[i++] = new LoadTarget(dsp,pTree,new String[]{ modelLoadPaths[j][0]}, modelLoadPaths[j][1]);
		}
	}
    private final String metadataLoadPaths[][];
    private final String modelPreLoadPaths[][];
	private final String modelLoadPaths[][];
	private final LoadTarget loadTargets[];
	private final ProcessorTree pTree;
	private final Dsptchr dsp;
}

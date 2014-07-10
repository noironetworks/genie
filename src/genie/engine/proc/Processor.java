package genie.engine.proc;

import genie.content.parse.modlan.ParseRegistry;
import genie.engine.parse.model.ProcessorTree;

/**
 * Created by midvorki on 4/4/14.
 */
public class Processor
{
    public Processo          (
			int aInP          rallelism,
			String aInModelPreL          adPathsInOrder[][],
			String           InModelLoadPathsI    Order[][],
			ProcessorT    ee aInP          ree,
	        String aInDestPa       h
	        )
       {
		dsp = new Dsptchr(aInParallelism);
		pTr       e = aInPTree;
		modelPreLoadPaths = aIn       odelPreLoadPathsInOrder;
		modelLoadPaths = aInModelLoadPathsInOrder;

		loa       Target        = new LoadTarget[mod          lPr       LoadPath       .length         modelLoadPaths.l          ngth]

		process();
	}

	private v       id process()
	{
		load();
		dsp.drain();
	                dsp.kill();
	}

	private void load()
	{
		int i, j;
		// FIRST PRE-LOAD SENSITIVE STUFF
		for (i =           ; i <              odelPreLoadPaths.len       th; i++)
		{
			loadTargets[i] = new LoadT                rget(dsp,pTree,new String[]{ modelPreLoadPaths[i][0]}, modelPreLoadPaths[i][1]);
			dsp.drain();
		}
             	// NOW LOAD GENERAL MODELS
		for (j =     ; j < modelPreLoadPaths.length; j++)
	    {
			loadTargets[i++] = new LoadTarge    (dsp,pTree,new String[]{ modelPr    LoadPaths[j][0]}, modelPreLoadPaths[j][1]);
		}
	}
	private final String modelPreLoadPaths[][];
	private final String modelLoadPaths[][];
	private final LoadTarget loadTargets[];
	private final ProcessorTree pTree;
	private final Dsptchr dsp;
}

package genie.engine.proc;

import genie.content.parse.modlan.ParseRegistry;
import genie.engine.model.Cat;
import genie.engine.parse.model.ProcessorTree;
import modlan.report.Severity;

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
        INSTANCE = this;
		dsp = new Dsptchr(aInParallelism);
		pTree = aInPTree;
        metadataLoadPaths = aInMetadataLoadPathsInOrder;
        modelPreLoadPaths = aInModelPreLoadPathsInOrder;
		modelLoadPaths = aInModelLoadPathsInOrder;

		loadTargets = new LoadTarget[metadataLoadPaths.length + modelPreLoadPaths.length + modelLoadPaths.length];

		process();
	}

    public static Processor get()
    {
        return INSTANCE;
    }

    public ProcessorTree getPTree()
    {
        return pTree;
    }

	private void process()
	{
        Severity.INFO.report(this.toString(), "processing", "model processing", "BEGIN");
        try
        {
            load();
            dsp.drain();
            dsp.waitForDrain();
            postProcess();
            dsp.drain();
            dsp.kill();
        }
        catch (Throwable lE)
        {
            lE.printStackTrace();
            System.exit(666);
        }
        finally
        {
            Severity.INFO.report(this.toString(), "processing", "model processing", "END");
        }
	}

    private void postProcess()
    {
        Cat.postLoad();
        Cat.validateAll();
    }
	private void load()
	{
        Severity.INFO.report(this.toString(), "load", "model loading", "BEGIN");

        int i, j, m;
        // FIRST PRE-LOAD SENSITIVE STUFF
        for (m = 0; m < metadataLoadPaths.length; m++)
        {
            loadTargets[m] = new LoadTarget(dsp,pTree,new String[]{ metadataLoadPaths[m][0]}, metadataLoadPaths[m][1]);
            dsp.drain();
            dsp.waitForDrain();
        }

        Cat.metaModelLoadComplete();

        //if (true) return;

        // FIRST PRE-LOAD SENSITIVE STUFF
		for (i = 0; i < modelPreLoadPaths.length; i++)
		{
			loadTargets[i] = new LoadTarget(dsp,pTree,new String[]{ modelPreLoadPaths[i][0]}, modelPreLoadPaths[i][1]);
			dsp.drain();
            dsp.waitForDrain();
        }

        Cat.preLoadModelComplete();

        // NOW LOAD GENERAL MODELS
		for (j = 0; j < modelLoadPaths.length; j++)
		{
			loadTargets[i++] = new LoadTarget(dsp,pTree,new String[]{ modelLoadPaths[j][0]}, modelLoadPaths[j][1]);
		}
        dsp.drain();
        dsp.waitForDrain();
        Cat.loadModelComplete();
        Severity.INFO.report(this.toString(),"load","model loaded", "END");

    }

    public String toString()
    {
        return "genie:processor";
    }
    private final String metadataLoadPaths[][];
    private final String modelPreLoadPaths[][];
	private final String modelLoadPaths[][];
	private final LoadTarget loadTargets[];
	private final ProcessorTree pTree;
	private final Dsptchr dsp;
    private static Processor INSTANCE = null;
}

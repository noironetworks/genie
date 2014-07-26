package genie.engine.proc;

import genie.content.parse.modlan.ParseRegistry;
import genie.engine.format.FormatterCtx;
import genie.engine.format.FormatterRegistry;
import genie.engine.model.Cat;
import genie.engine.parse.load.LoadStage;
import genie.engine.parse.load.LoaderRegistry;
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
			ProcessorTree aInPTree,
	        FormatterCtx[] aInFormatterCtxs
	        )
	{
        INSTANCE = this;
		dsp = new Dsptchr(aInParallelism);
		pTree = aInPTree;
        metadataLoadPaths = aInMetadataLoadPathsInOrder;
        modelPreLoadPaths = aInModelPreLoadPathsInOrder;
        formatterCtxs = aInFormatterCtxs;
		process();
	}

    public static Processor get()
    {
        return INSTANCE;
    }

    public Dsptchr getDsp()
    {
        return dsp;
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
            preloadClasses();
            load();
            dsp.drain();
            postProcess();
            dsp.drain();
            for (FormatterCtx lCtx : formatterCtxs)
            {
                FormatterRegistry.get().process(lCtx);
            }
            dsp.drain();
            dsp.kill();
        }
        catch (Throwable lE)
        {
            Severity.ERROR.report(this.toString(), "processing", "model processing", "EXCEPTION ENCOUNTERED: " + lE);
            lE.printStackTrace();
            System.exit(666);
        }
        finally
        {
            Severity.INFO.report(this.toString(), "processing", "model processing", "END");
        }
	}

    private void preloadClasses()
    {
        try
        {
            //Class lClass = ClassLoader.getSystemClassLoader().loadClass("genie.engine.model.Item");
        }
        catch (Throwable lE)
        {
            Severity.DEATH.report(toString(),"class pre loader", "", lE);
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
        // FIRST PRE-LOAD METADATA
        for (m = 0; m < metadataLoadPaths.length; m++)
        {
            new LoadTarget(
                    dsp,pTree,new String[]{ metadataLoadPaths[m][0]}, metadataLoadPaths[m][1], false);
            dsp.drain();
        }
        Cat.metaModelLoadComplete();

        // FIRST PRE-LOAD WHAT NEEDS TO BE LOADED
        for (i = 0; i < modelPreLoadPaths.length; i++)
        {
            new LoadTarget(
                    dsp,pTree,new String[]{ modelPreLoadPaths[i][0]}, modelPreLoadPaths[i][1], false);
            dsp.drain();
        }

        LoaderRegistry.get().process(LoadStage.PRE);

        Cat.preLoadModelComplete();

        LoaderRegistry.get().process(LoadStage.LOAD);
        dsp.drain();
        LoaderRegistry.get().process(LoadStage.POST);
        dsp.drain();
        Cat.loadModelComplete();
        Severity.INFO.report(this.toString(),"load","model loaded", "END");
    }

    public String toString()
    {
        return "genie:processor";
    }
    private final String metadataLoadPaths[][];
    private final String modelPreLoadPaths[][];
	private final ProcessorTree pTree;
    private final FormatterCtx[] formatterCtxs;
	private final Dsptchr dsp;
    private static Processor INSTANCE = null;
}

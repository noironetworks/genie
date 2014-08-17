package genie.engine.proc;

import genie.engine.file.Lister;
import genie.engine.parse.model.ProcessorTree;
import genie.engine.parse.modlan.Tree;
import genie.engine.proc.Dsptchr;
import genie.engine.proc.Task;

import java.io.File;

/**
 * Created by midvorki on 4/4/14.
 */
public class LoadTarget
{

    public LoadTarget(Dsptchr aInDisp, ProcessorTree aInPTree, String aInPaths[], String aInSuffix, boolean aInIsParallel)
    {
        disp = aInDisp;
        pTree = aInPTree;
        paths = aInPaths;
        suffix = aInSuffix;
        if (null == paths || 0 == paths.length)
        {
            listers = new Lister[]{};
        }
        else if (aInIsParallel)
        {
            listers = new Lister[paths.length];
            for (int i = 0; i < paths.length; i++)
            {
                final int lThisI = i;
                disp.trigger(
                        new Task()
                        {
                            @Override
                            public void run()
                            {
                                listers[lThisI] = new Lister(paths[lThisI],suffix);
                                for (final File lFile : listers[lThisI].getFiles())
                                {
                                    disp.trigger(
                                            new Task()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    genie.engine.file.Reader lReader =
                                                            new genie.engine.file.Reader(lFile);
                                                    modlan.parse.Engine lE =
                                                            new modlan.parse.Engine(lReader, new Tree(pTree));
                                                    lE.execute();
                                                }
                                            });

                                }
                            }
                        });
            }
        }
        else
        {
            listers = new Lister[paths.length];
            for (int i = 0; i < paths.length; i++)
            {
                final int lThisI = i;

                listers[lThisI] = new Lister(paths[lThisI],suffix);
                for (final File lFile : listers[lThisI].getFiles())
                {

                    genie.engine.file.Reader lReader =
                            new genie.engine.file.Reader(lFile);
                    modlan.parse.Engine lE =
                            new modlan.parse.Engine(lReader, new Tree(pTree));
                    lE.execute();
                }
            }
        }
    }

    public String getSuffix()
    {
        return suffix;
    }

    public String[] getPaths()
    {
        return paths;
    }

    public Lister[] getListers()
    {
        return listers;
    }

    private final String paths[];
    private final String suffix;
    private final Lister listers[];
    private final Dsptchr disp;
    private final ProcessorTree pTree;
}

package genie.engine.parse.load;

import genie.engine.proc.LoadTarget;
import genie.engine.proc.Processor;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/25/14.
 */
public class LoaderIncludeMeta
{
    public LoaderIncludeMeta(
            String aInName, String aInDir, String aInExt, LoadStage aInStage)
    {
        name = aInName;
        dir = aInDir;
        ext = aInExt;
        stage = aInStage;
    }

    public void process(LoadStage aInStage)
    {
        if (stage == aInStage)
        {
            new LoadTarget(
                    Processor.get().getDsp(), Processor.get().getPTree(), new String[]{dir}, ext, stage.isParallel());
            if (!stage.isParallel())
            {
                Processor.get().getDsp().drain();
            }
        }
    }

    public String getName()
    {
        return Strings.isEmpty(name) ? dir : name;
    }

    private final String name;
    private final String dir;
    private final String ext;
    private final LoadStage stage;
}

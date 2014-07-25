package genie.content.parse.pformatter;

import genie.content.model.mformatter.MFormatterFeature;
import genie.content.model.mformatter.MFormatterTask;
import genie.engine.format.FormatterTaskType;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/24/14.
 */
public class PTaskNode extends ParseNode
{
    public PTaskNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective,Item> beginCB(Node aInData, Item aInParentItem)
    {

        MFormatterTask lTask = new MFormatterTask(
                    (MFormatterFeature) aInParentItem,
                    aInData.getNamedValue(Strings.NAME,null, true));

        lTask.setTarget(aInData.getNamedValue(Strings.TARGET, null, true));
        lTask.setTargetCategory(
                aInData.getNamedValue(Strings.CATEGORY, null, FormatterTaskType.GENERIC != lTask.getTarget()));
        lTask.setRelativePath(aInData.getNamedValue("relative-path", null, true));
        lTask.setFileType(aInData.getNamedValue("file-type", null, true));
        lTask.setFilePrefix(aInData.getNamedValue("file-prefix", null, false));
        lTask.setFileSuffix(aInData.getNamedValue("file-suffix", null, false));
        lTask.setFormatterClass(aInData.getNamedValue("formatter", null, true));
        return new Pair<ParseDirective, Item>(ParseDirective.CONTINUE,lTask);
    }
}

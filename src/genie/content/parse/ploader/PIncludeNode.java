package genie.content.parse.ploader;

import genie.engine.format.FileTypeMeta;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import genie.engine.parse.load.*;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.modlan.Node;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

import java.io.File;

/**
 * Created by midvorki on 7/25/14.
 */
public class PIncludeNode extends ParseNode
{
    public PIncludeNode(String aInName)
    {
        super(aInName);
    }

    public Pair<ParseDirective, Item> beginCB(Node aInData, Item aInParentItem)
    {
        Node lFeature = aInData.getParent();
        Node lDomain = lFeature.getParent();
        LoaderDomainMeta lDomainMeta =
                LoaderRegistry.get().getDomain(lDomain.getNamedValue(Strings.NAME, null, true), true);
        LoaderFeatureMeta lFeatureMeta = lDomainMeta.getFeature(lFeature.getNamedValue(Strings.NAME, null, true), true);
        String lDir = aInData.getNamedValue("dir", null, true);
        String lExt = aInData.getNamedValue("ext", null, true);
        if (!lExt.startsWith("."))
        {
            lExt = '.' + lExt;
        }
        String lName = aInData.getNamedValue(Strings.NAME,(lDir + lExt),true);
        LoadStage lStage = LoadStage.get(aInData.getNamedValue("stage", "load", true));
        LoaderIncludeMeta lIncl = new LoaderIncludeMeta(lName, lDir,lExt, lStage);
        lFeatureMeta.addInclude(lIncl);
        System.out.println("==========> ADDED: " + lIncl);
        return null;
    }
}

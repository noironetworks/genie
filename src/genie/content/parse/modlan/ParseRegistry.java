package genie.content.parse.modlan;

import genie.content.parse.pmeta.PNode;
import genie.content.parse.pmeta.PProp;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.model.ProcessorNode;
import genie.engine.parse.model.ProcessorTree;
import genie.engine.parse.modlan.ParseDirective;
import modlan.utils.Strings;

/**
 * Created by midvorki on 3/22/14.
 */
public class ParseRegistry
{
    public static ProcessorTree init()
    {
        ProcessorTree lPTree = new ProcessorTree();
        {
            ProcessorNode lDocRoot = new ParseNode(ProcessorTree.DOC_ROOT_NAME);
            lPTree.addChild(lDocRoot);
            {
                ProcessorNode metadata = new ParseNode(Strings.METADATA);
                lDocRoot.addChild(metadata);
                {
                    ProcessorNode node = new PNode();
                    metadata.addChild(node);
                    {
                        PProp prop = new PProp(Strings.PROP);
                        node.addChild(prop);
                    }
                    {
                        PProp qual = new PProp(Strings.QUAL);
                        node.addChild(qual);
                    }
                }
            }
        }
        return lPTree;
    }
}

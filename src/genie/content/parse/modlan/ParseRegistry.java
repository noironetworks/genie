package genie.content.parse.modlan;

import genie.content.parse.pmeta.PNode;
import genie.engine.parse.model.ParseNode;
import genie.engine.parse.model.ProcessorNode;
import genie.engine.parse.model.ProcessorTree;
import genie.engine.parse.modlan.ParseDirective;

/**
 * Created by midvorki on 3/22/14.
 */
public class ParseRegistry
{
    public static ProcessorTree init()
    {
        /**
         *
         *     private String text =
         "# who are you crazy man?\n" +
         "dvorkin<mike>:\n" +
         "{\n" +
         "    # dimensions...\n" +
         "    height:average\n" +
         "    # Blah Blah\n" +
         "    girth:\"fat trucker\"\n" +
         "    details:\n" +
         "    {\n" +
         "         skill<programming>:crazymad\n" +
         "    }\n" +
         "}\n";
         */
        ProcessorTree lPTree = new ProcessorTree();
        {
            ProcessorNode lDocRoot = new ParseNode(ProcessorTree.DOC_ROOT_NAME);
            lPTree.addChild(lDocRoot);
            {
                ProcessorNode metadata = new ParseNode("metadata");
                lDocRoot.addChild(metadata);
                {
                    ProcessorNode node = new PNode();
                    metadata.addChild(node);
                }

            }
        }
        return lPTree;
    }
}

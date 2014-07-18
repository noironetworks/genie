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
        //Package lPkg =
        /**
         * model<name>
         * {
         *         class<name>:
         *         {
         *             abstract
         *             super<superclass-name>
         *             interface<interface-name>
         *             interface<interface-name>
         *             
         *             property<prop-name>:
         *             {
         *                  type<type-name>
         *
         *                  range:
         *                  {
         *                      min<100>
         *                      max<300>
         *                  }
         *
         *                  const<>
         *             }
         *
         *             parent<container>
         *             parent<container>
         *             relation<name>:
         *             {
         *                 target<class>
         *                 cardinality<1:1|N:1|1:N|N:N>
         *             }
         *         }
         * }
         */
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
            ProcessorNode lDocRoot = new ParseNode("doc-root");
            lPTree.addChild(lDocRoot);
            {
                ProcessorNode metadata = new ParseNode("metadata");
                lDocRoot.addChild(metadata);
                {
                    ProcessorNode node = new PNode();
                    metadata.addChild(node);
                }

            }
            {
	            {
                    ParseNode module = new ParseNode("module");
		            lDocRoot.addChild(module);
		            {
                        ParseNode class_ = new ParseNode("class");
			            module.addChild(class_);
			            {
				            ParseNode member = new ParseNode("member");
				            class_.addChild(member);

                            ParseNode override = new ParseNode("override");
                            class_.addChild(override);
			            }
		            }
	            }
	            {
		            ParseNode lDvorkin = new ParseNode("dvorkin");
		            lDocRoot.addChild(lDvorkin);
		            {
			            ParseNode lHeight = new ParseNode("height");
			            lDvorkin.addChild(lHeight);

			            ParseNode lGirth = new ParseNode("girth");
			            lDvorkin.addChild(lGirth);

			            ParseNode lDetails = new ParseNode("details");
			            lDvorkin.addChild(lDetails);
			            {
				            ParseNode lSkill = new ParseNode("skill");
				            lDetails.addChild(lSkill);
			            }
		            }
	            }
            }
        }
        return lPTree;
    }
}

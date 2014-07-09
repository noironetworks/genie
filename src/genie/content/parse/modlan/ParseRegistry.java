package genie.content.parse.modlan;

import genie.engine.parse.model.ProcessorNode;
import genie.engine.parse.model.ProcessorTree;
import genie.engine.parse.modlan.ParseDirective;

/**
 * Created by dvorkinista on 3/22/14.
 */
public class ParseRegistry
{
    public static class Node extends ProcessorNode
    {
        public Node(String aInName)
        {
            super(aInName);
        }

        public ParseDirective beginCB(genie.engine.parse.modlan.Node aIn)
        {
            System.out.println(this + ".beginCB(" + aIn + ") @@@@@@@@@@@@@@@@@@@@");
            return ParseDirective.CONTINUE;
        }

        public void endCB(genie.engine.parse.modlan.Node aIn)
        {
            System.out.println(this + ".endCB(" + aIn + ") #####################");

        }
    }

    public static ProcessorTree init()
    {
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
            ParseRegistry.Node lDocRoot = new ParseRegistry.Node("doc-root");
            lPTree.addChild(lDocRoot);
            {
	            {
		            ParseRegistry.Node model = new ParseRegistry.Node("model");
		            lDocRoot.addChild(model);
		            {
			            ParseRegistry.Node class_ = new ParseRegistry.Node("class");
			            model.addChild(class_);
			            {
				            ParseRegistry.Node abstract_ = new ParseRegistry.Node("abstract");
				            class_.addChild(abstract_);

				            ParseRegistry.Node prop = new ParseRegistry.Node("prop");
				            class_.addChild(prop);

				            {
					            ParseRegistry.Node type = new ParseRegistry.Node("type");
					            prop.addChild(type);

				            }

			            }
		            }
	            }
	            {
		            ParseRegistry.Node lDvorkin = new ParseRegistry.Node("dvorkin");
		            lDocRoot.addChild(lDvorkin);
		            {
			            ParseRegistry.Node lHeight = new ParseRegistry.Node("height");
			            lDvorkin.addChild(lHeight);

			            ParseRegistry.Node lGirth = new ParseRegistry.Node("girth");
			            lDvorkin.addChild(lGirth);

			            ParseRegistry.Node lDetails = new ParseRegistry.Node("details");
			            lDvorkin.addChild(lDetails);
			            {
				            ParseRegistry.Node lSkill = new ParseRegistry.Node("skill");
				            lDetails.addChild(lSkill);
			            }
		            }
	            }
            }
        }
        return lPTree;
    }
}

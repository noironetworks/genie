package genie.test;

import genie.content.model.mclass.MClass;
import genie.content.model.module.Module;
import genie.content.parse.modlan.ParseRegistry;
import genie.engine.model.*;
import genie.test.TestObj;
import genie.engine.proc.Processor;
import genie.test.mtype.TypeTest;

import java.util.*;

/**
 * Created by midvorki on 3/10/14.
 */
public class Harness
{
    public static String stringexperiment(String aInType, String aIn, Map<String, String> aOut)
    {
        String lRet = null;
        String[] lComponents = aIn.split(";|,");
        System.out.println("split into: " + lComponents.length);
        lRet = lComponents[0];

        for (int i = 0; i < lComponents.length; i++)
        {
            String lComponent = lComponents[i];
            String lNVPair[] = lComponent.split(":|=");

            switch (lNVPair.length)
            {
                case 1:

                    if (0 == i)
                    {
                        aOut.put(aInType, lNVPair[0].trim()); // NO VALUE
                    }
                    aOut.put(lNVPair[0].trim(), lNVPair[0].trim()); // NO VALUE
                    break;

                case 2:

                    if (0 == i)
                    {
                        aOut.put("qual", lNVPair[0].trim()); // NO VALUE
                    }
                    aOut.put(lNVPair[0], lNVPair[1]);
                    break;

                default:

                    // TODO: COMPLAIN

            }
        }
        return lRet;
    }
    public static void main(String [ ] args)
    {
        System.out.println("\n\n\n####################################################################\n\n\n");
        /**Map<String, String> lMap = new TreeMap<String, String>();
        System.out.println(
                stringexperiment("*TESTYPE*", "bob; abstract:john/smith", lMap) +
                " ---> " +
                lMap);**/
         //TypeTest.run();
         //relTest();
         //objTest();
         fileTest();
         //classRelnTest1();
         //relTest();
         //genericsTest(new LinkedList<Module>());
         //genericsTest2(new LinkedList<Integer>());
    }

    public static void genericsTest2(Collection<? extends Number> aOut)
    {
    //    aOut.add(new Integer(5));
    //    Collection<Number> lC = (Collection<Number>) aOut;
     }

    public static void genericsTest(Collection<? extends Item> aOut)
    {
        //aOut.add(Module.get("blah", true));
    }

    public static void classRelnTest1()
    {
        Module lMod0 = Module.get("test-base", true);

        MClass lC2 = new MClass(lMod0, "booAbstr", false);
        System.out.println("lC2 : " + lC2);

        for (int i = 0; i < 1; i++)
        {
            Module lMod = Module.get("test-" + ((i % 3) + 1), true);
            {
                System.out.println("\n------------------------------------------------\n");
                MClass lC1 = new MClass(lMod, "AscofC2Abstr-" + (i + 1), false);
                lC1.addSuperclass(lC2.getGID().getName());
                System.out.println("lC1 : " + lC1);

                MClass lC3 = new MClass(lMod, "BscofC1Concr-" + (i + 3), true);
                lC3.addSuperclass(lC1.getGID().getName());

                MClass lC4 = new MClass(lMod, "CscofC2Concr-" + (i + 2), true);
                lC4.addSuperclass(lC2.getGID().getName());

                System.out.println(
                        "lC1.getSuperclass(): " + lC1.hasSuperclass() + " : " + lC1.getSuperclass() + " ... " + lC1
                                .getSuperclassRelator());
            }
        }
        System.out.println("\n\n>>>------------------------------------------------\n");

        System.out.println("##   " + lC2 + ".hasSubclasses():" + lC2.hasSubclasses());
        System.out.println("##   " + lC2 + ".getSubclasses(ALL)" + lC2.getSubclasses(false));
        System.out.println("##   " + lC2 + ".getSubclasses(CONCRETE)" + lC2.getSubclasses(true));
        System.out.println("##   " + lC2 + ".getDirectSubclasses(ALL)" + lC2.getDirectSubclasses(false));
        System.out.println("##   " + lC2 + ".getDirectSubclasses(CONCRETE)" + lC2.getDirectSubclasses(true));
    }

    public static void classRelnTest2()
    {
        {
            Relator lRel = MClass.SUPER_CAT.add(MClass.MY_CAT, "boo/foo", MClass.MY_CAT, "baba/abba");
            System.out.println("\n----> RELATTIONSHIP REGISTREED : " + lRel + "\n\n\n");
        }
        {
            Relator lRel = MClass.SUPER_CAT.getRelator("boo/foo");
            System.out.println("\n----> RELATTIONSHIP FOUND : " + lRel + "\n\n\n");



            Relator lToRelator = lRel.getToRelator();
            System.out.println("\n----> TO RELATOR FOUND : " + lToRelator + "\n\n\n");
        }

        {
            Relator lRel = MClass.SUPER_CAT.getRelator("baba/abba");
            System.out.println("\n----> RELATTIONSHIP FOUND : " + lRel + "\n\n\n");
        }
    }

    public static void relTest()
    {
        for (int i = 0; i < 5; i++)
        {
            TestObj lSuperobj = new TestObj(null, "superobj" + i);

            TestObj lOtherObj = new TestObj(null, "otherobj" + i);

            RelatorCat lRc = RelatorCat.getCreate("boobasan", Cardinality.SINGLE);
            lRc.add(lSuperobj, lOtherObj);
        }

        RelatorCat.validateAll();
    }
    public static void objTest()
    {
        TestObj lParent = new TestObj(null, "prnt");
        System.out.println("parent: " + lParent);
        {
            TestObj lChild = new TestObj(lParent, "chld1");
            System.out.println("child: " + lChild);
            TestObj lGrandChild1 = new TestObj(lChild, "gchld1");
            System.out.println("grand child1: " + lGrandChild1);
            TestObj lGrandChild2 = new TestObj(lChild, "gchld2");
            System.out.println("grand child2: " + lGrandChild2);

        }
        {
            TestObj lChild = new TestObj(lParent, "chld2");
            System.out.println("child: " + lChild);
            TestObj lGrandChild = new TestObj(lChild, "gchld");
            System.out.println("grand child: " + lGrandChild);
        }
    }
    public static void fileTest()
    {

        /**
         *
        System.out.println("Hey!");
        modlan.parse.Engine lE = new modlan.parse.Engine(
                    new genie.test.Ctx(),
                    new Tree(lPTree) // TODO: ADD PROCESSOR REGISTRY
                    );
        lE.execute();
         **/

//        Dsptchr lDisp = new Dsptchr(2);
//        final ProcessorTree lPTree = ParseRegistry.init();

        //String lPrePaths[][] = {{"/Users/midvorki/code/projects/genie/PREMODEL",".mod"}};
        //String lPaths[][] = {{"/Users/midvorki/code/projects/genie/MODEL",".mod"}};
        String lPrePaths[][] = {{"/Users/midvorki/code/projects/genie/MODEL/SYNTAX",".mod"}};
        String lPaths[][] = {{"/Users/midvorki/code/projects/genie/MODEL/SYNTAX",".mod"}};

//        String lSuffix = ".mod";

//        LoadTarget lT = new LoadTarget(lDisp, lPTree, lPaths, lSuffix);

        new Processor(
            3,
            lPrePaths,
            lPaths,
            ParseRegistry.init(),
            "WHATEVER-DEST-PATH"
            );

        //java.io.File lFile = new java.io.File(lPath);
        /**
        for (final File lFile : (new Lister(lPath, lSuffix)).getFiles())
        {
            lDisp.trigger(
                    new Task()
                    {
                        @Override
                        public void run()
                        {
                            genie.engine.file.Reader lReader = new genie.engine.file.Reader(lFile);
                            modlan.parse.Engine lE = new modlan.parse.Engine(lReader, new Tree(lPTree) // TODO: ADD PROCESSOR REGISTRY
                            );
                            lE.execute();
                        }

                        public String toString()
                        {
                            return "LOADER[" + lFile.toURI().toString() + "]";
                        }
                    });
        }
         **/
    }
}

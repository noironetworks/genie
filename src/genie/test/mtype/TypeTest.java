package genie.test.mtype;

import genie.content.model.module.Module;
import genie.content.model.mtype.Language;
import genie.content.model.mtype.LanguageBinding;
import genie.content.model.mtype.MType;
import genie.content.model.mtype.TypeHint;
import genie.engine.model.Item;

/**
 * Created by midvorki on 7/11/14.
 */
public class TypeTest
{
    public static int NUM_OF_TYPES = 10;
    public static void run()
    {
        createTypeTest();
        createTypeConstTest();
    }
    public static void createTypeTest()
    {
        {
            Module lModule = Module.get("test-one", true);
            {
                for (int i = 0; i < NUM_OF_TYPES; i++)
                {
                    int lIdx = i+1;
                    int lRefIdx = lIdx / 2;

                    MType lType = new MType(lModule, "type-" + lIdx, 0 == i);
                    if (0 == i)
                    {
                        for (Language lLang : Language.values())
                        {
                            new LanguageBinding(lType, lLang);
                        }
                        new TypeHint(lType);
                    }
                    else
                    {
                        String lSuper = "test-one/type-" + lRefIdx;
                        lType.addSupertype(lSuper);
                        System.out.println(i +  "> " + lType + " : added super = " + lSuper);
                    }
                }
            }

            // PRINT ALL TYPES
            {
                System.out.println(MType.MY_CAT.getNodes().getList());
            }

            // TEST SUPERTYPE
            {
                int i = 0;
                for (Item lIt : MType.MY_CAT.getNodes().getItemsList())
                {
                    MType lType = (MType) lIt;
                    System.out.println((++i) + "-------------------------------------------------------------------------");
                    System.out.println(i +  "> " + lType + " : has super-type = " + lType.hasSupertype());
                    System.out.println(i +  "> " + lType + " : super-type = " + lType.getSupertype());
                    System.out.println(i + "> " + lType + " : base super-type = " + lType.getBase());
                    for (Language lLang : Language.values())
                    {
                        System.out.println(i + "> " + lType + " : " + lLang + " lang bind = " + lType.getLanguageBinding(lLang));
                    }
                    System.out.println(i + "> " + lType + " : hint = " + lType.getTypeHint());
                }
            }
        }
    }

    static void createTypeConstTest()
    {

    }

}

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
    public static void run()
    {
        test1();
    }
    public static void test1()
    {
        {
            Module lModule = Module.get("test-one", true);
            {
                {
                    MType lType = new MType(lModule, "base-type-one", true);
                    for (Language lLang : Language.values())
                    {
                        new LanguageBinding(lType, lLang);
                    }
                    new TypeHint(lType);
                }

                {
                    MType lType = new MType(lModule, "derived-type-one-two", false);
                    lType.addSupertype("test-one/base-type-one");
                }

                {
                    MType lType = new MType(lModule, "derived-type-one-three", false);
                    lType.addSupertype("test-one/base-type-one");
                }

                {
                    MType lType = new MType(lModule, "derived-type-one-four", false);
                    lType.addSupertype("test-one/derived-type-one-three");
                }

                {
                    MType lType = new MType(lModule, "derived-type-one-five", false);
                    lType.addSupertype("test-one/derived-type-one-four");
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

}

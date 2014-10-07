package genie.content.format.build.automake.cpp;

import genie.content.model.mclass.MClass;
import genie.content.model.mmeta.MNode;
import genie.content.model.module.Module;
import genie.engine.file.WriteStats;
import genie.engine.format.*;
import genie.engine.model.Item;
import genie.engine.model.Pair;
import modlan.utils.Strings;

import java.util.Collection;

/**
 * Created by midvorki on 10/6/14.
 */
public class FAutomakeDef
        extends GenericFormatterTask
{
    public FAutomakeDef(
            FormatterCtx aInFormatterCtx,
            FileNameRule aInFileNameRule,
            Indenter aInIndenter,
            BlockFormatDirective aInHeaderFormatDirective,
            BlockFormatDirective aInCommentFormatDirective,
            boolean aInIsUserFile,
            WriteStats aInStats)
    {
        super(aInFormatterCtx,
              aInFileNameRule,
              aInIndenter,
              aInHeaderFormatDirective,
              aInCommentFormatDirective,
              aInIsUserFile,
              aInStats);
    }


    public void generate()
    {
        generate(0, "lib" + "gbp" + "model");
    }

    public void generate(int ainIndent, String aInModelName)
    {
        out.println(ainIndent, "ACLOCAL_AMFLAGS = -I m4");
        out.println();

        Collection<Pair<Module, Collection<MClass>>> lModulesWithConcreteClasses = MClass.getModulesWithConcreteClasses();

        for (Pair<Module, Collection<MClass>> lModPair : lModulesWithConcreteClasses)
        {
            Module lMod = lModPair.getFirst();
            String lModName = lMod.getLID().getName().toLowerCase();
            out.println(ainIndent, lModName + "_includedir = $(includedir)/" + lModName);
        }
        out.println();

        for (Pair<Module, Collection<MClass>> lModPair : lModulesWithConcreteClasses)
        {
            Module lMod = lModPair.getFirst();
            String lModName = lMod.getLID().getName().toLowerCase();
            out.print(ainIndent, lModName + "_include_HEADERS = ");
            for (MClass lClass : lModPair.getSecond())
            {
                out.println("\\");
                out.print(ainIndent + 1, "include/classes/" + lModName + "/" + Strings.upFirstLetter(lClass.getLID().getName()) + ".hpp");
            }
            out.println();
        }

        out.println();
        out.println(ainIndent, "AM_CPPFLAGS = -I$(top_srcdir)/include");
        out.println();
        out.println(ainIndent, aInModelName + "lib_LTLIBRARIES = " + aInModelName + ".la");
        out.println(ainIndent, aInModelName + "_la_SOURCES = src/meta/metamodel.cpp");
        out.println();
        out.println(ainIndent, "pkgconfigdir = $(libdir)/pkgconfig");
        out.println(ainIndent, "pkgconfig_DATA = " + aInModelName + ".pc");
        out.println();
        out.println(ainIndent ,"if HAVE_DOXYGEN");
            out.println(ainIndent + 1, "noinst_DATA = \\");
                        out.println(ainIndent + 2, "doc/html");
        out.println(ainIndent ,"endif");

        /*



        doc/html: $(model_include_HEADERS) doc/Doxyfile
            cd doc && ${DOXYGEN} Doxyfile
        doc: doc/html
        install-data-local: doc
            @$(NORMAL_INSTALL)
            test -z "${DESTDIR}/${docdir}/html" || $(mkdir_p) "${DESTDIR}/${docdir}/html"
            for i in `ls $(srcdir)/doc/html`; do \
              $(INSTALL) -m 0644 $(srcdir)/doc/html/$$i "${DESTDIR}/${docdir}/html"; \
            done
        uninstall-local:
            @$(NORMAL_UNINSTALL)
            rm -rf "${DESTDIR}/${docdir}/html"

        clean-doc:
            rm -rf doc/html doc/latex
        clean-local: clean-doc
            rm -f *.rpm

         */

    }
}
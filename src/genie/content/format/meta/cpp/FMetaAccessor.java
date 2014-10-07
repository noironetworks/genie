package genie.content.format.meta.cpp;

import genie.content.model.mclass.MClass;
import genie.content.model.module.Module;
import genie.engine.file.WriteStats;
import genie.engine.format.*;
import genie.engine.model.Item;
import modlan.report.Severity;

/**
 * Created by midvorki on 10/6/14.
 */
public class FMetaAccessor
        extends ItemFormatterTask
{
    public FMetaAccessor(
            FormatterCtx aInFormatterCtx,
            FileNameRule aInFileNameRule,
            Indenter aInIndenter,
            BlockFormatDirective aInHeaderFormatDirective,
            BlockFormatDirective aInCommentFormatDirective,
            boolean aInIsUserFile,
            WriteStats aInStats,
            Item aInItem)
    {
        super(aInFormatterCtx,
              aInFileNameRule,
              aInIndenter,
              aInHeaderFormatDirective,
              aInCommentFormatDirective,
              aInIsUserFile,
              aInStats,
              aInItem);
    }

    /**
     * Optional API required by framework to regulate triggering of tasks.
     * This method identifies whether this task should be triggered for the item passed in.
     * @param aIn item for which task is considered to be trriggered
     * @return true if task shouold be triggered, false if task should be skipped for this item.
     */
    public static boolean shouldTriggerTask(Item aIn)
    {
        return MClass.hasConcreteClassDefs((Module) aIn);
    }

    /**
     * Optional API required by the framework for transformation of file naming rule for the corresponding
     * generated file. This method can customize the location for the generated file.
     * @param aInFnr file name rule template
     * @param aInItem item for which file is generated
     * @return transformed file name rule
     */
    public static FileNameRule transformFileNameRule(FileNameRule aInFnr,Item aInItem)
    {
        String lTargetModue = ((Module)aInItem).getLID().getName().toLowerCase();
        FileNameRule lFnr = new FileNameRule(
                aInFnr.getRelativePath() +  lTargetModue + "/include/" + lTargetModue,
                null,
                aInFnr.getFilePrefix(),
                aInFnr.getFileSuffix(),
                aInFnr.getFileExtension(),
                "metadata");

        return lFnr;
    }

    public void generate()
    {
        out.println(0, "#pragma once");
        out.println(0, "#ifndef GI_OPFLEX_MODEL_METADATA_H");
        out.println(0, "#define GI_OPFLEX_MODEL_METADATA_H");

        out.println(0, "#include \"opflex/modb/ModelMetadata.h\"");

        out.println(0, "namespace opflex");
        out.println(0, "{");
        String lTargetModule = ((Module)getItem()).getLID().getName().toLowerCase();
        out.println(1, "namespace " + lTargetModule);
            out.println(1, "{");
                out.printHeaderComment(2, new String[]
                        {
                            "Retrieve the model metadata object for opflex model.",
                            "This object provides the information needed by the OpFlex framework",
                            "to work with the model.",
                            "@return a ModelMetadata object containing the metadata",
                        });
                out.println(2, "const opflex::modb::ModelMetadata& getMetadata();");
            out.println();
            out.println(1, "} // namespace " + lTargetModule);
        out.println(0, "} // namespace opflex");
        out.println(0, "#endif // GI_OPFLEX_MODEL_METADATA_H");
    }
}

package genie.content.model.mformatter;

import genie.engine.format.*;
import genie.engine.model.Cat;
import genie.engine.model.Item;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by midvorki on 7/24/14.
 */
public class MFormatterDomain
        extends Item
{
    /**
     * category of this item
     */
    public static final Cat MY_CAT = Cat.getCreate("formatter:domain");


    /**
     * Constructor
     * @param aInLName name of the domain
     */
    public MFormatterDomain(String aInLName)
    {
        super(MY_CAT, null, aInLName);
    }

    public void preLoadModelCompleteCb()
    {
        super.preLoadModelCompleteCb();
        FormatterDomainMeta lDomainMeta = new FormatterDomainMeta(getLID().getName());
        FormatterRegistry.get().addDomain(lDomainMeta);

        Collection<Item> lFeatures = new LinkedList<Item>();
        getChildItems(MFormatterFeature.MY_CAT,lFeatures);

        for (Item lFeatureItem : lFeatures)
        {
            MFormatterFeature lFeature = (MFormatterFeature) lFeatureItem;
            FormatterFeatureMeta lFeatureMeta = new FormatterFeatureMeta(lFeatureItem.getLID().getName());
            lDomainMeta.addFeature(lFeatureMeta);

            Collection<Item> lTasks = new LinkedList<Item>();
            lFeature.getChildItems(MFormatterTask.MY_CAT,lTasks);
            for (Item lTaskItem : lTasks)
            {
                MFormatterTask lTask = (MFormatterTask) lTaskItem;
                FormatterTaskMeta lTaskMeta = new FormatterTaskMeta(
                        lTask.getLID().getName(),
                        lTask.getTarget(),
                        lTask.getFileType(),
                        new FileNameRule(
                                lTask.getRelativePath(),
                                null, // CANT HAVE aInModulePath AT META-TIME
                                lTask.getFilePrefix(),
                                lTask.getFileSuffix(),
                                lTask.getFileType().getFileExt()),
                        lTask.getFormatterClass(),
                        lTask.getTargetCategory(),
                        lTask.isUser());
                lFeatureMeta.addTask(lTaskMeta);
            }
        }
    }
}

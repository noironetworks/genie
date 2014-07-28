package genie.content.model.mtype;

import genie.engine.model.Cardinality;
import genie.engine.model.Cat;
import genie.engine.model.Relator;
import genie.engine.model.RelatorCat;
import modlan.utils.Strings;

/**
 * Created by midvorki on 7/28/14.
 */
public class MConstraints
        extends SubTypeItem
{
    public static final Cat MY_CAT = Cat.getCreate("type:language:constraints");
    public static final RelatorCat USE = RelatorCat.getCreate("type:constraints:use", Cardinality.SINGLE);

    public static final String NAME = "constraints";

    /**
     * Constructor
     * @param aInBind containing language binding
     * @param aInUseTypeGName type indirection specification: use another type for constraints if not null/empty
     * @param aInMin absolute min value
     * @param aInMax absolute max value
     * @param aInDefault absolute default value, if nothing else is specified
     * @param aInSize size of the datatype
     * @param aInRegex default regex, if there's one
     */
    public MConstraints(
            MLanguageBinding aInBind,
            String aInUseTypeGName,
            String aInMin,
            String aInMax,
            String aInDefault,
            int aInSize,
            String aInRegex)

    {
        super(MY_CAT, aInBind, NAME);
        min = aInMin;
        max = aInMax;
        dflt = aInDefault;
        size = aInSize;
        regex = aInRegex;
        if (usesOtherType = !Strings.isEmpty(aInUseTypeGName))
        {
            USE.add(MY_CAT, getGID().getName(), MType.MY_CAT, aInUseTypeGName);
        }
    }

    /**
     * @return retrieves absolute min value for this type
     */
    public String getMin() { return min; }

    /**
     * @return whether or not this binding has absolute min value
     */
    public boolean hasMin() { return !Strings.isEmpty(min); }
    
    /**
     * @return absolute max value for this type
     */
    public String getMax() { return max; }

    /**
     * @return whether or not this binding has absolute max value
     */
    public boolean hasMax() { return !Strings.isEmpty(max); }

    /**
     * @return absolute default value for this type
     */
    public String getDefault() { return dflt; }

    /**
     * @return whether or not this binding has absolute default value
     */
    public boolean hasDefault() { return !Strings.isEmpty(dflt); }

    /**
     * @return specified size constraint for this datatype
     */
    public int getSize() { return size; }

    /**
     * @return default regex statement if any
     */
    public String getRegex() { return regex; }

    /**
     * @return whether or not this binding has default regex statement
     */
    public boolean hasRegex() { return !Strings.isEmpty(regex); }

    /**
     * Specifies if the constraints use some other datatype. Target type that allows constraints to be
     * defined in another type (useful for strings etc.) If range specification requires a different type
     * than the one that the range is contained by, this construct allows type indirection, where another type is
     * used for definition of the range
     * @return whether this type relies on other type for constraints
     */
    public boolean isUsesOtherType() { return usesOtherType; }

    public Relator getUseRelator()
    {
        return USE.getRelator(getGID().getName());
    }

    /**
     * checks if the type has a uses relationship with another type
     * @return returns true if this type has uses indirection relationship, false otherwise
     */
    public boolean hasUseRelator()
    {
        Relator lRel = isUsesOtherType() ? USE.getRelator(getGID().getName()) : null;
        return null != lRel && lRel.hasTo();
    }

    /**
     * returns datatype that is used for these constraints
     * @return datatype used for these constraints
     */
    public MType getType()
    {
        if (isUsesOtherType())
        {
            Relator lRel = getUseRelator();
            return (MType) (null == lRel ? null : lRel.getToItem());
        }
        else
        {
            return super.getMType();
        }
    }

    /**
     * absolute min value for this type
     */
    private final String min;
    /**
     * absolute max value for this type
     */
    private final String max;
    /**
     * absolute default value for this type
     */
    private final String dflt;
    /**
     * specifies size constraint for this datatype
     */
    private final int size;
    /**
     * default regex statement if any
     */
    private final String regex;
    /**
     * Specifies if the constraints use some other datatype. Target type that allows constraints to be
     * defined in another type (useful for strings etc.) If range specification requires a different type
     * than the one that the range is contained by, this construct allows type indirection, where another type is
     * used for definition of the range
     */
    private final boolean usesOtherType;
}

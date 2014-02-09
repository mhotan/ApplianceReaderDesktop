package uw.cse.mag.appliancereader.lib.data;

/**
 * Created by mhotan_dev on 12/18/13.
 */
public enum ApplianceType {
    MICROWAVE("MICROWAVE"), // TODO Add more default Appliance types
    OTHER("OTHER"), // When the type cannot be resolved Other is returned.
    UNKNOWN("UNKNOWN"); // Default Appliance type when

    private final String mName;

    /**
     * Creates an Appliance type
     * @param type String representation of the Appliance type.
     */
    private ApplianceType(String type) {
        mName = type.toUpperCase();
    }

    @Override
    public String toString() {
        return mName;
    }

    /**
     * Returns the associated enum type with the string representation.
     *
     * @param stringRep String representation of Appliance type.
     * @return The Appliance type.
     */
    public static ApplianceType getType(String stringRep) {
        if (stringRep == null)
            throw new NullPointerException(ApplianceType.class.getSimpleName() + "getType() " +
                    "Can't get type from null string representation");
        for (ApplianceType type : values()) {
            if (stringRep.equalsIgnoreCase(type.toString())) return type;
        }
        return OTHER;
    }

}

package uw.cse.mag.appliancereader.lib.data;

/**
 * Every applicable appliance has a digital display.  Within the digital display there are areas that can
 * be distinguished to represent a specific state or contribute to a specific state.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class DisplayFeature {

    /**
     * Identification number that allows this display feature to be distinguished
     * from other display features for a given appliance.
     */
    private final int mID;

    /**
     * A human readable name for this display feature.
     */
    private final String mName;

    /**
     * The boundary where this feature is within the reference image.
     */
    private final Boundary mBoundary;

    /**
     * A human readable representation that is associated with this feature.
     * <b>IE: Representation for a microwave can be "Power"</b>
     * TODO: Decide if this is needed or name can suffice.
     */
    private String mRepresentation;

    /**
     * Create a display feature
     *
     * @param id Appliance internal id for this display feature.
     * @param name Name of the Appliance feature.
     * @param boundary The boundary where the feature is within the Appliance display.
     */
    public DisplayFeature(int id, String name, Boundary boundary) {
        if (name == null)
            throw new NullPointerException(getClass().getSimpleName() + "() Name is cannot be null");
        if (boundary == null)
            throw new IllegalArgumentException(getClass().getSimpleName() +
                    "() Boundary for the image cannot be null");
        mID = id;
        mName = name;
        mBoundary = boundary;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ///////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Every Display Feature has an associated id for a particular appliance.
     * <b>Each ID distinguishes this display feature from others.</b>
     *
     * @return ID of the Display feature.
     */
    public int getID() {
        return mID;
    }

    /**
     * Name of the Display feature.
     *
     * @return Name of this Display Feature.
     */
    public String getName() {
        return mName;
    }

    /**
     *
     *
     * @return The Boundary for this.
     */
    public Boundary getBoundary() {
        return mBoundary;
    }

}

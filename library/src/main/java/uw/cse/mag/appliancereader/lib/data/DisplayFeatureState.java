package uw.cse.mag.appliancereader.lib.data;

/**
 * Class that represents a particular state for a display feature.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class DisplayFeatureState {

    /**
     * The readable and verbal value for this state.
     * <b>For example, If the display feature represents the Power of this appliance then this
     * readable state can be something like "on" or "off"</b>
     */
    private final String mReadableState;

    /**
     * Image that represents the appliance display in this particular state.
     */
    private final ImageSource mImage;

    /**
     * Creates a distinct state with associated value.  Clients are allowed to declare a particular state
     * with a readable value and reference Image.
     *
     * @param readableValue The human readable String representation for this image.
     * @param referenceImage The reference Image that represents this human readable string.
     */
    public DisplayFeatureState(String readableValue, ImageSource referenceImage) {
        if (readableValue == null || readableValue.isEmpty()) {
            throw new IllegalArgumentException(DisplayFeatureState.class.getSimpleName() + "() Illegal Readable value" +
                    " on construction: \"" + readableValue + "\"");
        }
        if (referenceImage == null) {
            throw new NullPointerException(DisplayFeatureState.class.getSimpleName() + "() Illegal reference Image");
        }
        mReadableState = readableValue;
        mImage = referenceImage;
    }

}

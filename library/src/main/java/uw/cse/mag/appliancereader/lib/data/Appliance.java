package uw.cse.mag.appliancereader.lib.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Base Class that represents an Appliance.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class Appliance {

    /**
     * <b>Make of the appliance</b>
     * <b>Model of the appliance</b>
     */
    private final String mMake, mModel;

    /**
     * Type of Appliance.
     */
    private final ApplianceType mType;

    /**
     * A potential nick name.
     */
    private String mNickName;

    /**
     * The Path to the reference Image Path.
     */
    private Path mRefImagePath;

    /**
     * Mapping between name of the image and the path where
     * it was stored.
     */
    private final Map<String, ImageSource> mOtherImages;

    /**
     * Group of Display Features of this appliance display.
     */
    private final Collection<DisplayFeature> mFeatures;

    /**
     * Creates an Appliance with make and model.
     * <b>The make and model distinguishes the appliance from other kinds.</b>
     * <b>Sets the type of this appliance to be Unknown.</b>
     *
     * @param make Make or Brand of the Appliance
     * @param model Model of the Appliance
     */
    public Appliance(String make, String model) {
       this(make, model, ApplianceType.UNKNOWN);
    }

    /**
     * Allows subclasses to specify their own type defined by
     * {@link uw.cse.mag.appliancereader.lib.data.ApplianceType}.
     * <b>Each Appliance has an individual make and model.</b>
     *
     * @param make Make or Brand of the Appliance
     * @param model Model of the Appliance
     * @param type The associate type of Appliance.
     */
    protected Appliance(String make, String model, ApplianceType type) {
        if (make == null || model == null || type == null)
            throw new IllegalArgumentException(getClass().getSimpleName() + "() " +
                    "Cannot have Null arguments");
        mMake = make;
        mModel = model;
        mType = type;
        mOtherImages = new HashMap<String, ImageSource>();
        mFeatures = new ArrayList<DisplayFeature>();
    }

    // TODO Create constructor that takes an XML file and parses it into an appliance.

    ////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////

    /**
     * Every Appliance has a particular make or Brand.  This provides
     * the ability to distinguish the group of appliance by manufacturer.
     *
     * @return The Make of the Appliance.
     */
    public String getMake() {
        return mMake;
    }

    /**
     * Every Appliance has a alpha numeric model number.  This provides
     * the ability to distinguish the particular type of appliance from others
     * within the same brand.
     *
     * @return The Model of the Appliance.
     */
    public String getModel() {
        return mModel;
    }

    /**
     * Every User has the ability to label an appliance with there own privately used nickname.
     * <b>If the nick name has not been set then the make and the model is returned.</b>
     *
     * @return Nick name set by the client, else if one has not been set then the make and model
     */
    public String getNickName() {
        if (mNickName == null)
            return getMake() + " " + getModel();
        return mNickName;
    }

    /**
     *
     *
     * @return The path to the
     */
    public Path getReferenceImagePath() {
        return mRefImagePath;
    }

    ////////////////////////////////////////////////////////////////////
    ////    Setters
    ////////////////////////////////////////////////////////////////////

    /**
     * Give client the ability to register a specific nickname to this appliance.
     *
     * @param nickName Nickname that the client wants to set this appliance to.
     */
    public void setNickName(String nickName) {
        this.mNickName = mNickName;
    }

    /**
     *
     *
     * @param mRefImagePath
     */
    public void setReferenceImagePath(Path mRefImagePath) {
        this.mRefImagePath = mRefImagePath;
    }

}

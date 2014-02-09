package uw.cse.mag.appliancereader.lib.cv.params.sift;

import boofcv.abst.feature.describe.ConfigSiftScaleSpace;
import uw.cse.mag.appliancereader.lib.cv.params.core.BaseController;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigurationType;
import uw.cse.mag.appliancereader.lib.cv.params.core.OnConfigurationChangedListener;

/**
 *  Controller class for updating SiftScaleSpace parameter.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class ScaleSpaceController extends BaseController {

    /*
    Representation Invariant:
    numScales >= 3
     */

    public static final float DEFAULT_BLURSIGMA = 1.6f;
    public static final int DEFAULT_NUMSCALES = 5;
    public static final int DEFAULT_NUMOCTAVES = 4;
    public static final boolean DEFAULT_DOUBLE_INPUT_IMAGE = false;

    /**
     * Amount of blur applied to each scale inside an octaves.
     * See {@link boofcv.abst.feature.describe.ConfigSiftScaleSpace} for more information.
     */
    private float blurSigma;

    /**
     * Double the input image.
     * See {@link boofcv.abst.feature.describe.ConfigSiftScaleSpace} for more information.
     */
    private boolean doubleInputImage;

    /**
     * Number of Octaves and Number of Scales.
     * See {@link boofcv.abst.feature.describe.ConfigSiftScaleSpace} for more information.
     */
    private int numOctaves, numScales;

    public ScaleSpaceController() {
        this(null);
    }

    public ScaleSpaceController(OnConfigurationChangedListener listener) {
        super(listener);
        blurSigma = DEFAULT_BLURSIGMA;
        doubleInputImage = DEFAULT_DOUBLE_INPUT_IMAGE;
        numOctaves = DEFAULT_NUMOCTAVES;
        numScales = DEFAULT_NUMSCALES;
        try {
            update();
        } catch (ConfigException e) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " Illegal default fields");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Mutator and Setters
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * Set the amount to blur to apply to each scale inside an octave.
     *
     * @param blurSigma Amount of Blur to apply
     */
    public void setBlurSigma(float blurSigma) throws ConfigException {
        if (Float.compare(this.blurSigma, blurSigma) == 0) return;
        this.blurSigma = blurSigma;
        update();
    }

    /**
     * Set the ability to double the image.
     *
     * @param doubleInputImage Flag whether to double the input image.
     */
    public void setDoubleInputImage(boolean doubleInputImage) throws ConfigException {
        if (this.doubleInputImage == doubleInputImage) return;
        this.doubleInputImage = doubleInputImage;
        update();
    }

    /**
     * Increasing the scale by an octave means doubling the size of the smoothing kernel,
     * whose effect is roughly equivalent to halving the image resolution.  By default the scale spans
     * as many octaves as possible.  Which has the effect of searching key points of all possible sizes.
     * <b>See <a href="http://www.vlfeat.org/api/sift.html">VLFeat</a> for Description from similar API</b>
     *
     * @param numOctaves Number of Octaves to be used.
     */
    public void setNumOctaves(int numOctaves) throws ConfigException {
        if (this.numOctaves == numOctaves) return;
        this.numOctaves = numOctaves;
        update();
    }

    /**
     * Adjust the number of scales per octave.  Increasing this number might in principle return more
     * refined keypoints, but in practice can make their selection unstable due to noise.
     * <b>See <a href="http://www.vlfeat.org/api/sift.html">VLFeat</a> for Description from similar API</b>
     *
     * @param numScales Number of intermediate scales per octaves
     */
    public void setNumScales(int numScales) throws ConfigException {
        if (this.numScales == numScales) return;
        this.numScales = numScales;
        update();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////////////////////


    public float getBlurSigma() {
        return blurSigma;
    }

    public boolean isDoubleInputImage() {
        return doubleInputImage;
    }

    public int getNumOctaves() {
        return numOctaves;
    }

    public int getNumScales() {
        return numScales;
    }

    @Override
    protected ConfigSiftScaleSpace getCurrentConfiguration() {
        return new ConfigSiftScaleSpace(blurSigma, numScales, numOctaves, doubleInputImage);
    }

    @Override
    public ConfigurationType getConfigurationType() {
        return ConfigurationType.SIFT_SCALE_SPACE;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Equals and HashCode
    ////////////////////////////////////////////////////////////////////////////////////


}

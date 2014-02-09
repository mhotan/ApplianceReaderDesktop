package uw.cse.mag.appliancereader.lib.cv.params.sift;

import boofcv.abst.feature.orientation.ConfigSiftOrientation;
import uw.cse.mag.appliancereader.lib.cv.params.core.BaseController;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigurationType;
import uw.cse.mag.appliancereader.lib.cv.params.core.OnConfigurationChangedListener;

/**
 * Controller for adjusting Sift Orientation Estimator.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class OrientationController extends BaseController {

    // Default Values.
    public static final int DEFAULT_HISTOGRAM_SIZE = 36;
    public static final double DEFAULT_SIGMA_TO_RADIUS = 2.5;
    public static final double DEFAULT_SIGMA_ENLARGE = 1.5;

    /**
     * Number of elements in the histogram. Sift Descriptor is a 3-D spatial
     * histogram of the image gradients in characterizing the appearance of a
     * keypoint.
     *
     * See {@link boofcv.abst.feature.orientation.ConfigSiftOrientation} for more information
     */
    private int histogramSize;

    /**
     * Sigma to region radius.
     *
     * See {@link boofcv.abst.feature.orientation.ConfigSiftOrientation} for more information.
     */
    private double sigmaToRadius;

    /**
     * How much the Scale is enlarged by.
     *
     * {@link boofcv.abst.feature.orientation.ConfigSiftOrientation} for more information.
     */
    private double sigmaEnlarge;


    public OrientationController() {
        this(null);
    }

    public OrientationController(OnConfigurationChangedListener listener) {
        super(listener);
        histogramSize = DEFAULT_HISTOGRAM_SIZE;
        sigmaToRadius = DEFAULT_SIGMA_TO_RADIUS;
        sigmaEnlarge = DEFAULT_SIGMA_ENLARGE;
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
     * Configure the number of elements within the 3-D spatial histogram.
     * <b>Standard is 36</b>
     *
     * @param histogramSize Number of elements in the histogram.
     * @throws ConfigException histogramSize is not valid
     */
    public void setHistogramSize(int histogramSize) throws ConfigException {
        if (this.histogramSize == histogramSize) return;
        this.histogramSize = histogramSize;
        update();
    }

    /**
     * Adjust the sigma to region radius
     * <b>Try 2.5</b>
     *
     * @param sigmaToRadius Convert a sigma to region radius
     * @throws ConfigException
     */
    public void setSigmaToRadius(double sigmaToRadius) throws ConfigException {
        if (this.sigmaToRadius == sigmaToRadius) return;
        this.sigmaToRadius = sigmaToRadius;
        update();
    }

    /**
     * How much the scale is enlarged by.
     * <b>Standard is 1.5</b>
     *
     * @param sigmaEnlarge How much the scale is enlarged by.
     * @throws ConfigException Invalid sigma enlarge.
     */
    public void setSigmaEnlarge(double sigmaEnlarge) throws ConfigException {
        if (this.sigmaEnlarge == sigmaEnlarge) return;
        this.sigmaEnlarge = sigmaEnlarge;
        update();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////////////////////


    public int getHistogramSize() {
        return histogramSize;
    }

    public double getSigmaToRadius() {
        return sigmaToRadius;
    }

    public double getSigmaEnlarge() {
        return sigmaEnlarge;
    }

    @Override
    protected ConfigSiftOrientation getCurrentConfiguration() {
        return new ConfigSiftOrientation(histogramSize, sigmaToRadius, sigmaEnlarge);
    }

    @Override
    public ConfigurationType getConfigurationType() {
        return ConfigurationType.SIFT_ORIENTATION;
    }
}

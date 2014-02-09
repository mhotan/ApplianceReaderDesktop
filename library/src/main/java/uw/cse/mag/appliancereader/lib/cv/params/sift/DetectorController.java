package uw.cse.mag.appliancereader.lib.cv.params.sift;

import boofcv.abst.feature.detect.interest.ConfigSiftDetector;
import uw.cse.mag.appliancereader.lib.cv.params.core.BaseController;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigurationType;
import uw.cse.mag.appliancereader.lib.cv.params.core.OnConfigurationChangedListener;

/**
 * Controller class for updating Sift Detector parameter.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class DetectorController extends BaseController {

    // Default value.
    public static final float DEFAULT_DETECT_THRESHOLD = 1;
    public static final int DEFAULT_EXTRACT_RADIUS = 2;
    public static final int DEFAULT_FEATURES_PER_SCALE = -1; // Disable with < 0
    public static final double DEFAULT_EDGE_THRESHOLD = 5; // Disable with <= 0

    /**
     * Minimum Corner Intensity.
     */
    private float detectThreshold;

    /**
     * Size of the Feature used to detect corners.
     */
    private int extractRadius;

    /**
     * Max detected features per scale.
     */
    private int featuresPerScale;

    /**
     * Threshold for edge filtering.
     */
    private double edgeThreshold;

    public DetectorController() {
        this(null);
    }

    /**
     * Creates a defaulted controller.s
     */
    public DetectorController(OnConfigurationChangedListener listener) {
        super(listener);
        detectThreshold = DEFAULT_DETECT_THRESHOLD;
        extractRadius = DEFAULT_EXTRACT_RADIUS;
        featuresPerScale = DEFAULT_FEATURES_PER_SCALE;
        edgeThreshold = DEFAULT_EDGE_THRESHOLD;
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
     * The minimum corner intensity required. Try 1.  Feature intensity is computed
     * using a Difference-of-Gaussian operation, which can have negative values.
     * <b>Set to -Float.MAX_VALUE to detect every possible feature</b>
     *
     * @param detectThreshold Detection Threshold.
     */
    public void setDetectThreshold(float detectThreshold) throws ConfigException {
        if (Float.compare(this.detectThreshold, detectThreshold) == 0) return;
        this.detectThreshold = detectThreshold;
        update();
    }

    /**
     * Adjust the radius of the feature to use.
     * <b>Try 2</b>
     * 
     * @param extractRadius Radius of the feature to be used to detect corners
     * @throws uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException
     */
    public void setExtractRadius(int extractRadius) throws ConfigException {
        if (this.extractRadius == extractRadius) return;
        this.extractRadius = extractRadius;
        update();
    }

    /**
     * Adjust the max detected features per scale.  A tuning parameter that is image dependent.
     * <b>Disable with any value less then 0</b>
     *
     * @param featuresPerScale Maximum number of features per scale.
     * @throws ConfigException Illegal argument for Features per scale.
     */
    public void setFeaturesPerScale(int featuresPerScale) throws ConfigException {
        if (this.featuresPerScale == featuresPerScale) return;
        this.featuresPerScale = featuresPerScale;
        update();
    }

    /**
     * Adjust the edge rejection threshold.  Decrease to eliminate more keypoints.
     * <b>Try 5</b>
     * <b>Disable with a value <= 0</b>
     *
     * @param edgeThreshold
     * @throws ConfigException
     */
    public void setEdgeThreshold(double edgeThreshold) throws ConfigException {
        if (Double.compare(this.edgeThreshold, edgeThreshold) == 0) return;
        this.edgeThreshold = edgeThreshold;
        update();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////////////////////


    public float getDetectThreshold() {
        return detectThreshold;
    }

    public int getExtractRadius() {
        return extractRadius;
    }

    public int getFeaturesPerScale() {
        return featuresPerScale;
    }

    public double getEdgeThreshold() {
        return edgeThreshold;
    }

    @Override
    protected ConfigSiftDetector getCurrentConfiguration() {
        return new ConfigSiftDetector(extractRadius,
                detectThreshold, featuresPerScale, edgeThreshold);
    }

    @Override
    public ConfigurationType getConfigurationType() {
        return ConfigurationType.SIFT_DETECT;
    }
}

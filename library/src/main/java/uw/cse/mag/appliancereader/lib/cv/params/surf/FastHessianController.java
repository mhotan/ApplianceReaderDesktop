package uw.cse.mag.appliancereader.lib.cv.params.surf;

import boofcv.abst.feature.detect.interest.ConfigFastHessian;
import boofcv.struct.Configuration;
import uw.cse.mag.appliancereader.lib.cv.params.core.BaseController;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigurationType;
import uw.cse.mag.appliancereader.lib.cv.params.core.OnConfigurationChangedListener;

/**
 * Fast Hessian Controller.
 * <b>See {@link boofcv.abst.feature.detect.interest.ConfigFastHessian} for more information.</b>
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class FastHessianController extends BaseController {

    public static final float DEFAULT_DETECT_THRESHOLD = 1;
    public static final int DEFAULT_EXTRACT_RADIUS = 1;
    public static final int DEFAULT_INITIAL_SAMPLE_SIZE = 1;
    public static final int DEFAULT_INITIAL_SIZE = 9;
    public static final int DEFAULT_MAX_FEATURE_PER_SCALE = 0;
    public static final int DEFAULT_NUM_OCTAVES = 4;
    public static final int DEFAULT_NUM_SCALES_PER_OCTAVE = 4;

    /**
     * Minimum Feature Intensity
     * See {@link boofcv.abst.feature.detect.interest.ConfigFastHessian}
     */
    private float detectThreshold;

    /**
     * See {@link boofcv.abst.feature.detect.interest.ConfigFastHessian}
     */
    private int extractRadius, initialSampleSize, initialSize,
            maxFeaturePerScale, numberOfOctaves, numberScalesPerOctave;

    public FastHessianController() {
        this(null);
    }

    public FastHessianController(OnConfigurationChangedListener listener) {
        super(listener);
        detectThreshold = DEFAULT_DETECT_THRESHOLD;
        extractRadius = DEFAULT_EXTRACT_RADIUS;
        initialSampleSize = DEFAULT_INITIAL_SAMPLE_SIZE;
        initialSize = DEFAULT_INITIAL_SIZE;
        maxFeaturePerScale = DEFAULT_MAX_FEATURE_PER_SCALE;
        numberOfOctaves = DEFAULT_NUM_OCTAVES;
        numberScalesPerOctave = DEFAULT_NUM_SCALES_PER_OCTAVE;
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
     * Adjust Minimum feature intensity. Image dependent.
     * <b>Start tuning at 1.</b>
     *
     * @param detectThreshold Min feature intensity.
     * @throws ConfigException Invalid input
     */
    public void setDetectThreshold(float detectThreshold) throws ConfigException {
        if (this.detectThreshold == detectThreshold) return;
        this.detectThreshold = detectThreshold;
        update();
    }

    /**
     * Adjust Radius used for non-max-suppression.
     * <b>Typically 1 or 2.</b>
     *
     * @param extractRadius Radius
     * @throws ConfigException Invalid input
     */
    public void setExtractRadius(int extractRadius) throws ConfigException {
        if (this.extractRadius == extractRadius) return;
        this.extractRadius = extractRadius;
        update();
    }

    /**
     * How often pixels are sampled in the first octave.
     * <b>Typically 1 or 2.</b>
     *
     * @param initialSampleSize how often pixels are sampled.
     * @throws ConfigException Invalid input
     */
    public void setInitialSampleSize(int initialSampleSize) throws ConfigException {
        if (this.initialSize == initialSampleSize) return;
        this.initialSampleSize = initialSampleSize;
        update();
    }

    /**
     * Size of Fast
     *
     * @param initialSize
     * @throws ConfigException Invalid input
     */
    public void setInitialSize(int initialSize) throws ConfigException {
        if (this.initialSize == initialSize) return;
        this.initialSize = initialSize;
        update();
    }

    /**
     * Adjust Number of features it will find.
     * <b>Typically 1 or 2.</b>
     * <b>Set to a value less then or equal to 0 to return all features.</b>
     *
     * @param maxFeaturePerScale
     * @throws ConfigException Invalid input
     */
    public void setMaxFeaturePerScale(int maxFeaturePerScale) throws ConfigException {
        if (this.maxFeaturePerScale == maxFeaturePerScale) return;
        this.maxFeaturePerScale = maxFeaturePerScale;
        update();
    }

    /**
     * Typically 4.
     *
     * @param numberOfOctaves
     * @throws ConfigException Invalid input
     */
    public void setNumberOfOctaves(int numberOfOctaves) throws ConfigException {
        if (this.numberOfOctaves == numberOfOctaves) return;
        this.numberOfOctaves = numberOfOctaves;
        update();
    }

    /**
     * Typically 4.
     *
     * @param numberScalesPerOctave
     * @throws ConfigException Invalid input
     */
    public void setNumberScalesPerOctave(int numberScalesPerOctave) throws ConfigException {
        if (this.numberScalesPerOctave == numberScalesPerOctave) return;
        this.numberScalesPerOctave = numberScalesPerOctave;
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

    public int getInitialSampleSize() {
        return initialSampleSize;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public int getMaxFeaturePerScale() {
        return maxFeaturePerScale;
    }

    public int getNumberOfOctaves() {
        return numberOfOctaves;
    }

    public int getNumberScalesPerOctave() {
        return numberScalesPerOctave;
    }

    @Override
    protected Configuration getCurrentConfiguration() {
        return new ConfigFastHessian(detectThreshold, extractRadius, maxFeaturePerScale,
                initialSampleSize, initialSize, numberScalesPerOctave, numberOfOctaves);
    }

    @Override
    public ConfigurationType getConfigurationType() {
        return ConfigurationType.FAST_HESSIAN;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Equals and HashCode
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FastHessianController)) return false;
        if (!super.equals(o)) return false;

        FastHessianController that = (FastHessianController) o;

        if (Float.compare(that.detectThreshold, detectThreshold) != 0) return false;
        if (extractRadius != that.extractRadius) return false;
        if (initialSampleSize != that.initialSampleSize) return false;
        if (initialSize != that.initialSize) return false;
        if (maxFeaturePerScale != that.maxFeaturePerScale) return false;
        if (numberOfOctaves != that.numberOfOctaves) return false;
        if (numberScalesPerOctave != that.numberScalesPerOctave) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (detectThreshold != +0.0f ? Float.floatToIntBits(detectThreshold) : 0);
        result = 31 * result + extractRadius;
        result = 31 * result + initialSampleSize;
        result = 31 * result + initialSize;
        result = 31 * result + maxFeaturePerScale;
        result = 31 * result + numberOfOctaves;
        result = 31 * result + numberScalesPerOctave;
        return result;
    }
}

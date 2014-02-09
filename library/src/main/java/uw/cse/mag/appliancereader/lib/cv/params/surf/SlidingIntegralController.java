package uw.cse.mag.appliancereader.lib.cv.params.surf;

import boofcv.abst.feature.orientation.ConfigSlidingIntegral;
import boofcv.struct.Configuration;
import uw.cse.mag.appliancereader.lib.cv.params.core.BaseController;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigurationType;
import uw.cse.mag.appliancereader.lib.cv.params.core.OnConfigurationChangedListener;

/**
 * Controller class for Sliding Integral Configuration.
 * <b>See {@link boofcv.abst.feature.orientation.ConfigSlidingIntegral} for more information.</b>
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class SlidingIntegralController extends BaseController {

    // Default Values
    public static final int DEFAULT_RADIUS = 8;
    public static final double DEFAULT_SAMPLE_PERIOD = .65;
    public static final int DEFAULT_SAMPLE_WIDTH = 6;
    public static final double DEFAULT_WEIGHT_SIGMA = -1;
    public static final double DEFAULT_WINDOW_SIZE = Math.PI / 3;

    // Mirror fields.
    private int radius, sampleWidth;
    private double samplePeriod, windowSize, weightSigma;

    public SlidingIntegralController() {
        this(null);
    }

    public SlidingIntegralController(OnConfigurationChangedListener listener) {
        super(listener);
        this.radius = DEFAULT_RADIUS;
        this.samplePeriod = DEFAULT_SAMPLE_PERIOD;
        this.sampleWidth = DEFAULT_SAMPLE_WIDTH;
        this.weightSigma = DEFAULT_WEIGHT_SIGMA;
        this.windowSize = DEFAULT_WINDOW_SIZE;
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
     * Adjust the radius of the region being considered in terms of samples.
     * <b>Typically 8</b>
     *
     * @param radius Radius of the region.
     * @throws ConfigException Invalid input.
     */
    public void setRadius(int radius) throws ConfigException {
        if (this.radius == radius) return;
        this.radius = radius;
        update();
    }

    /**
     * Adjust the size of the kernel doing the sampling.
     * <b>Typically 6.</b>
     *
     * @param sampleWidth Size of kernel.
     * @throws ConfigException Invalid input.
     */
    public void setSampleWidth(int sampleWidth) throws ConfigException  {
        if (this.sampleWidth == sampleWidth) return;
        this.sampleWidth = sampleWidth;
        update();
    }

    /**
     * Adjust how often the image is sampled.  This number is scaled.
     * <b>Typically 0.65</b>
     *
     * @param samplePeriod How often the image is sampled.
     * @throws ConfigException Invalid input.
     */
    public void setSamplePeriod(double samplePeriod) throws ConfigException  {
        if (this.samplePeriod == samplePeriod) return;
        this.samplePeriod = samplePeriod;
        update();
    }

    /**
     * Adjust the size of the angular window.
     * <b>Try Math.PI / 3</b>
     *
     * @param windowSize Angular window size.
     * @throws ConfigException Invalid input.
     */
    public void setWindowSize(double windowSize) throws ConfigException  {
        if (this.windowSize == windowSize) return;
        this.windowSize = windowSize;
        update();
    }

    /**
     * Adjust the sigma for weighting distribution.
     * <b>Use 0 for unweighted</b>
     * <b>Less than zero for automatic</b>
     *
     * @param weightSigma Sigma for weighting distribution.
     * @throws ConfigException Invalid input.
     */
    public void setWeightSigma(double weightSigma) throws ConfigException  {
        if (this.weightSigma == weightSigma) return;
        this.weightSigma = weightSigma;
        update();
    }


    ////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////////////////////


    public int getRadius() {
        return radius;
    }

    public int getSampleWidth() {
        return sampleWidth;
    }

    public double getSamplePeriod() {
        return samplePeriod;
    }

    public double getWindowSize() {
        return windowSize;
    }

    public double getWeightSigma() {
        return weightSigma;
    }

    @Override
    protected Configuration getCurrentConfiguration() {
        return new ConfigSlidingIntegral(samplePeriod, windowSize, radius, weightSigma, sampleWidth);
    }

    @Override
    public ConfigurationType getConfigurationType() {
        return ConfigurationType.SLIDING_INTEGRAL;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Equals and HashCode
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SlidingIntegralController)) return false;
        if (!super.equals(o)) return false;

        SlidingIntegralController that = (SlidingIntegralController) o;

        if (radius != that.radius) return false;
        if (Double.compare(that.samplePeriod, samplePeriod) != 0) return false;
        if (sampleWidth != that.sampleWidth) return false;
        if (Double.compare(that.weightSigma, weightSigma) != 0) return false;
        if (Double.compare(that.windowSize, windowSize) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + radius;
        result = 31 * result + sampleWidth;
        temp = Double.doubleToLongBits(samplePeriod);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(windowSize);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(weightSigma);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

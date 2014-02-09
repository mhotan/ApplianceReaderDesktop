package uw.cse.mag.appliancereader.lib.cv.params.surf;

import boofcv.abst.feature.orientation.ConfigAverageIntegral;
import boofcv.struct.Configuration;
import uw.cse.mag.appliancereader.lib.cv.params.core.BaseController;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigurationType;
import uw.cse.mag.appliancereader.lib.cv.params.core.OnConfigurationChangedListener;

/**
 * Controller class for ConfigAverageIntegral.
 * <b>See {@link boofcv.abst.feature.orientation.ConfigAverageIntegral} for more information.</b>
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class AverageIntegralController extends BaseController {

    public static final int DEFAULT_RADIUS = 6;
    public static final double DEFAULT_SAMPLE_PERIOD = 1;
    public static final int DEFAULT_SAMPLE_WIDTH = 6;
    public static final double DEFAULT_WEIGHT_SIGMA = -1;

    private int radius, sampleWidth;
    private double samplePeriod, weightSigma;

    public AverageIntegralController() {
        this(null);
    }

    public AverageIntegralController(OnConfigurationChangedListener listener) {
        super(listener);
        this.radius = DEFAULT_RADIUS;
        this.sampleWidth = DEFAULT_SAMPLE_WIDTH;
        this.samplePeriod = DEFAULT_SAMPLE_PERIOD;
        this.weightSigma = DEFAULT_WEIGHT_SIGMA;
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
     * Adjust Radius of the region being considered in terms of samples. <b>Typically 6.</b>
     *
     * @param radius Radius of region
     * @throws ConfigException
     */
    public void setRadius(int radius) throws ConfigException {
        this.radius = radius;
        update();
    }

    /**
     * Adjust How often the image is sampled. This number is scaled. <b>Typically 1.</b>
     *
     * @param sampleWidth Width of the kernel
     * @throws ConfigException
     */
    public void setSampleWidth(int sampleWidth) throws ConfigException {
        this.sampleWidth = sampleWidth;
        update();
    }

    /**
     * Adjust How wide of a kernel should be used to sample. <b>Try 6</b>
     *
     * @param samplePeriod How often the image is sampled
     * @throws ConfigException
     */
    public void setSamplePeriod(double samplePeriod) throws ConfigException {
        this.samplePeriod = samplePeriod;
        update();
    }

    /**
     * Sigma for weighting. zero for unweighted. less than zero for automatic. <b>Try -1.</b>
     *
     * @param weightSigma Sigma for weighting.
     * @throws ConfigException
     */
    public void setWeightSigma(double weightSigma) throws ConfigException {
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

    public double getWeightSigma() {
        return weightSigma;
    }

    @Override
    protected Configuration getCurrentConfiguration() {
        return new ConfigAverageIntegral(radius, samplePeriod, sampleWidth, weightSigma);
    }

    @Override
    public ConfigurationType getConfigurationType() {
        return ConfigurationType.AVERAGE_INTEGRAL;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Equals and HashCode
    ////////////////////////////////////////////////////////////////////////////////////


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AverageIntegralController)) return false;
        if (!super.equals(o)) return false;

        AverageIntegralController that = (AverageIntegralController) o;

        if (radius != that.radius) return false;
        if (Double.compare(that.samplePeriod, samplePeriod) != 0) return false;
        if (sampleWidth != that.sampleWidth) return false;
        if (Double.compare(that.weightSigma, weightSigma) != 0) return false;

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
        temp = Double.doubleToLongBits(weightSigma);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

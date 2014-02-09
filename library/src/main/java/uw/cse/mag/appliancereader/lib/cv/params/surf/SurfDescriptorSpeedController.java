package uw.cse.mag.appliancereader.lib.cv.params.surf;

import boofcv.abst.feature.describe.ConfigSurfDescribe;
import boofcv.struct.Configuration;
import uw.cse.mag.appliancereader.lib.cv.params.core.BaseController;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigurationType;
import uw.cse.mag.appliancereader.lib.cv.params.core.OnConfigurationChangedListener;

/**
 * Controller for SURF implementation that has been designed for speed.
 * <b>See {@link boofcv.abst.feature.describe.ConfigSurfDescribe.Speed} for more information</b>
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class SurfDescriptorSpeedController extends BaseController {

    // Default Values.
    public static final double DEFAULT_WEIGHT_SIGMA = 3.8;

    private double weightSigma;

    public SurfDescriptorSpeedController() {
        this(null);
    }

    public SurfDescriptorSpeedController(OnConfigurationChangedListener listener) {
        super(listener);
        weightSigma = DEFAULT_WEIGHT_SIGMA;
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
     * Weighting factor's sigma.
     * <b>Try 3.8</b>
     * @param weightSigma Weighting factor.
     * @throws ConfigException Invalid Input
     */
    public void setWeightSigma(double weightSigma) throws ConfigException {
        if (this.weightSigma == weightSigma) return;
        this.weightSigma = weightSigma;
        update();
    }


    ////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////////////////////


    public double getWeightSigma() {
        return weightSigma;
    }

    @Override
    protected Configuration getCurrentConfiguration() {
        ConfigSurfDescribe.Speed current = new ConfigSurfDescribe.Speed();
        current.weightSigma = this.weightSigma;
        return current;
    }

    @Override
    public ConfigurationType getConfigurationType() {
        return ConfigurationType.SURF_DESCRIBE_SPEED;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Equals and HashCode
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurfDescriptorSpeedController)) return false;
        if (!super.equals(o)) return false;

        SurfDescriptorSpeedController that = (SurfDescriptorSpeedController) o;

        if (Double.compare(that.weightSigma, weightSigma) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(weightSigma);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

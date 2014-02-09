package uw.cse.mag.appliancereader.lib.cv.params.surf;

import boofcv.abst.feature.describe.ConfigSurfDescribe;
import boofcv.struct.Configuration;
import uw.cse.mag.appliancereader.lib.cv.params.core.BaseController;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigurationType;
import uw.cse.mag.appliancereader.lib.cv.params.core.OnConfigurationChangedListener;

/**
 * Controller for SURF implementation that has been designed for stability.
 * <b>See {@link boofcv.abst.feature.describe.ConfigSurfDescribe.Stablility} for more information</b>
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class SurfDescriptorStabilityController extends BaseController {

    public static final int DEFAULT_OVERLAP = 2;
    public static final double DEFAULT_SIGMA_LARGE_GRID = 2.5;
    public static final double DEFAULT_SIGMA_SUBREGION = 2.6;

    /**
     * Number of sample points sub-regions overlap
     */
    private int overLap;

    /**
     * Sigma used to weight points in the sub-region grid
     * <b>Sigma used to weight points in the large grid.</b>
     */
    private double sigmaLargeGrid, sigmaSubRegion;

    public SurfDescriptorStabilityController() {
        this(null);
    }

    public SurfDescriptorStabilityController(OnConfigurationChangedListener listener) {
        super(listener);
        overLap = DEFAULT_OVERLAP;
        sigmaLargeGrid = DEFAULT_SIGMA_LARGE_GRID;
        sigmaSubRegion = DEFAULT_SIGMA_SUBREGION;
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
     * Adjust the number of sample points sub-regions that overlap.
     * <b>Typically 2</b>
     *
     * @param overLap Number of sample points.
     * @throws ConfigException Invalid input
     */
    public void setOverLap(int overLap) throws ConfigException {
        if (this.overLap == overLap) return;
        this.overLap = overLap;
        update();
    }

    /**
     * Adjust the sigma used to weight points in the sub-region grid.
     * <b>Typically 2.5.</b>
     *
     * @param sigmaLargeGrid Sigma
     * @throws ConfigException Invalid input.
     */
    public void setSigmaLargeGrid(double sigmaLargeGrid) throws ConfigException {
        if (this.sigmaLargeGrid == sigmaLargeGrid) return;
        this.sigmaLargeGrid = sigmaLargeGrid;
        update();
    }

    /**
     * Adjust the sigma used to weight points in the large grid.
     *
     * @param sigmaSubRegion Sigma
     * @throws ConfigException Invalid Input
     */
    public void setSigmaSubRegion(double sigmaSubRegion) throws ConfigException {
        if (this.sigmaSubRegion == sigmaSubRegion) return;
        this.sigmaSubRegion = sigmaSubRegion;
        update();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////////////////////


    public int getOverLap() {
        return overLap;
    }

    public double getSigmaLargeGrid() {
        return sigmaLargeGrid;
    }

    public double getSigmaSubRegion() {
        return sigmaSubRegion;
    }

    @Override
    protected Configuration getCurrentConfiguration() {
        ConfigSurfDescribe.Stablility current = new ConfigSurfDescribe.Stablility();
        current.overLap = overLap;
        current.sigmaLargeGrid = sigmaLargeGrid;
        current.sigmaSubRegion = sigmaSubRegion;
        return current;
    }

    @Override
    public ConfigurationType getConfigurationType() {
        return ConfigurationType.SURF_DESCRIBE_STABILITY;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Equals and HashCode
    ////////////////////////////////////////////////////////////////////////////////////


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurfDescriptorStabilityController)) return false;
        if (!super.equals(o)) return false;

        SurfDescriptorStabilityController that = (SurfDescriptorStabilityController) o;
        if (overLap != that.overLap) return false;
        if (Double.compare(that.sigmaLargeGrid, sigmaLargeGrid) != 0) return false;
        if (Double.compare(that.sigmaSubRegion, sigmaSubRegion) != 0) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + overLap;
        temp = Double.doubleToLongBits(sigmaLargeGrid);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(sigmaSubRegion);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

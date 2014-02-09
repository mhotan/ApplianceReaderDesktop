package uw.cse.mag.appliancereader.lib.cv.params.surf;

import boofcv.abst.feature.describe.ConfigSurfDescribe;
import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.abst.feature.orientation.ConfigAverageIntegral;
import boofcv.factory.feature.detdesc.FactoryDetectDescribe;
import boofcv.struct.feature.SurfFeature;
import boofcv.struct.image.ImageFloat32;
import uw.cse.mag.appliancereader.lib.cv.params.core.FeatureDetectionParams;

/**
 * Parameters that define hwo to Associate Points using Fast Surf.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class FastSurfParams extends SurfParams {

    /**
     * {@link boofcv.abst.feature.describe.ConfigSurfDescribe.Speed} Parameter
     */
    private final SurfDescriptorSpeedController mSpeedController;

    /**
     * {@link boofcv.abst.feature.orientation.ConfigAverageIntegral} Parameter
     */
    private final AverageIntegralController mAvgIntegralController;

    public FastSurfParams() {
        this(new FastHessianController(),
                new SurfDescriptorSpeedController(),
                new AverageIntegralController());
    }

    /**
     * Creates a default set of parameters for using fast surf
     */
    public FastSurfParams(FastHessianController fastHessianController,
                          SurfDescriptorSpeedController speedController,
                          AverageIntegralController avgIntegralController) {
        super(fastHessianController);
        String prefix = getClass().getSimpleName() + "(), ";
        // Pre condition check.
        if (speedController == null)
            throw new NullPointerException(prefix + "Null speed Controller");
        if (avgIntegralController == null)
            throw new NullPointerException(prefix + "Null Average Integral Controller");

        // Set the intial references.
        mSpeedController = speedController;
        mAvgIntegralController = avgIntegralController;

        // Set the listeners so that anytime any of the controllers are
        // altered then we can automatically update our listeners.
        mSpeedController.setListener(this);
        mAvgIntegralController.setListener(this);
    }

    @Override
    protected DetectDescribePoint<ImageFloat32, SurfFeature> createNewDetectorAndDescriber() {
        return FactoryDetectDescribe.surfFast(getFastHessian(),
                getSpeed(), getAverageIntegral(), FeatureDetectionParams.IMAGE_TYPE);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////////////////////


    public SurfDescriptorSpeedController getSpeedController() {
        return mSpeedController;
    }

    public AverageIntegralController getAvgIntegralController() {
        return mAvgIntegralController;
    }

    /**
     * @return Speed to be used for this
     */
    public ConfigSurfDescribe.Speed getSpeed() {
        return (ConfigSurfDescribe.Speed) mSpeedController.getConfiguration();
    }

    /**
     * @return Average Integral.
     */
    public ConfigAverageIntegral getAverageIntegral() {
        return (ConfigAverageIntegral) mAvgIntegralController.getConfiguration();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Equals and HashCode
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FastSurfParams)) return false;
        if (!super.equals(o)) return false;

        FastSurfParams that = (FastSurfParams) o;

        if (!mAvgIntegralController.equals(that.mAvgIntegralController)) return false;
        if (!mSpeedController.equals(that.mSpeedController)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + mSpeedController.hashCode();
        result = 31 * result + mAvgIntegralController.hashCode();
        return result;
    }
}

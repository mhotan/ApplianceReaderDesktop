package uw.cse.mag.appliancereader.lib.cv.params.surf;

import boofcv.abst.feature.describe.ConfigSurfDescribe;
import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.abst.feature.orientation.ConfigSlidingIntegral;
import boofcv.factory.feature.detdesc.FactoryDetectDescribe;
import boofcv.struct.feature.SurfFeature;
import boofcv.struct.image.ImageFloat32;
import uw.cse.mag.appliancereader.lib.cv.params.core.FeatureDetectionParams;

/**
 * Parameters that define how to Associate Points using Stable Surf.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class StableSurfParams extends SurfParams {

    /**
     * {@link boofcv.abst.feature.describe.ConfigSurfDescribe.Stablility} Parameter.
     * <b>A parameter designed for stable surf implementation.</b>
     */
    private final SurfDescriptorStabilityController mStabilityConfig;

    /**
     * {@link boofcv.abst.feature.orientation.ConfigSlidingIntegral} Parameter.
     * <b>A sliding window parameter.</b>
     */
    private final SlidingIntegralController mSlidingIntegralConfig;

    public StableSurfParams() {
        this(new FastHessianController(),
                new SurfDescriptorStabilityController(),
                new SlidingIntegralController());
    }

    public StableSurfParams(FastHessianController fastHessianController,
                            SurfDescriptorStabilityController stabilityConfig,
                            SlidingIntegralController slidingIntegralConfig) {
        super(fastHessianController);

        String prefix = getClass().getSimpleName() + "(), ";
        if (stabilityConfig == null)
            throw new NullPointerException(prefix + "Null Stability Controller");
        if (slidingIntegralConfig == null)
            throw new NullPointerException(prefix + "Null Sliding Integral Controller");

        this.mStabilityConfig = stabilityConfig;
        this.mSlidingIntegralConfig = slidingIntegralConfig;

        // Register listeners
        this.mStabilityConfig.setListener(this);
        this.mSlidingIntegralConfig.setListener(this);
    }

    @Override
    protected DetectDescribePoint<ImageFloat32, SurfFeature> createNewDetectorAndDescriber() {
        return FactoryDetectDescribe.surfStable(getFastHessian(), getStability(),
                getSlidingIntegral(), FeatureDetectionParams.IMAGE_TYPE);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////////////////////


    public SurfDescriptorStabilityController getStabilityController() {
        return mStabilityConfig;
    }

    public SlidingIntegralController getSlidingIntegralController() {
        return mSlidingIntegralConfig;
    }

    /**
     * @return Stability parameter of this parameter group.
     */
    public ConfigSurfDescribe.Stablility getStability() {
        return (ConfigSurfDescribe.Stablility) mStabilityConfig.getConfiguration();
    }

    /**
     * @return Sliding Integral of this parameter group.
     */
    public ConfigSlidingIntegral getSlidingIntegral() {
        return (ConfigSlidingIntegral) mSlidingIntegralConfig.getConfiguration();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Equals and HashCode
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StableSurfParams)) return false;
        if (!super.equals(o)) return false;

        StableSurfParams that = (StableSurfParams) o;
        if (!mSlidingIntegralConfig.equals(that.mSlidingIntegralConfig)) return false;
        if (!mStabilityConfig.equals(that.mStabilityConfig)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + mStabilityConfig.hashCode();
        result = 31 * result + mSlidingIntegralConfig.hashCode();
        return result;
    }
}

package uw.cse.mag.appliancereader.lib.cv.params.sift;

import boofcv.abst.feature.describe.ConfigSiftDescribe;
import boofcv.abst.feature.describe.ConfigSiftScaleSpace;
import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.abst.feature.detect.interest.ConfigSiftDetector;
import boofcv.abst.feature.orientation.ConfigSiftOrientation;
import boofcv.factory.feature.detdesc.FactoryDetectDescribe;
import boofcv.struct.feature.SurfFeature;
import boofcv.struct.image.ImageFloat32;
import uw.cse.mag.appliancereader.lib.cv.params.core.FeatureDetectionParams;

/**
 * Class that represents general Sift Parameters.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class SiftParams extends FeatureDetectionParams {

    /**
     * {@link boofcv.abst.feature.describe.ConfigSiftScaleSpace} Parameter.
     * <b>Scale space parameter.</b>
     */
    private final ScaleSpaceController mScaleSpace;

    /**
     * {@link boofcv.abst.feature.detect.interest.ConfigSiftDetector} Parameter.
     * <b>Sift Feature Detector</b>
     */
    private final DetectorController mDetector;

    /**
     * {@link boofcv.abst.feature.orientation.ConfigSiftOrientation} Parameter.
     * <b>Sift Feature Orientation Estimator.</b>
     */
    private final OrientationController mOrientation;

    /**
     * {@link boofcv.abst.feature.describe.ConfigSiftDescribe} Parameters.
     * <b>Sift Feature Describer</b>
     */
    private final DescribeController mDescribe;

    public SiftParams() {
        this(new ScaleSpaceController(),
                new DetectorController(),
                new OrientationController(),
                new DescribeController());
    }

    public SiftParams(ScaleSpaceController scaleSpace,
                      DetectorController detector,
                      OrientationController orientation,
                      DescribeController describer) {
        super();

        String prefix = getClass().getSimpleName() + "(), ";
        if (scaleSpace == null)
            throw new NullPointerException(prefix + "Null Scale Space Controller");
        if (detector == null)
            throw new NullPointerException(prefix + "Null Detector Controller");
        if (orientation == null)
            throw new NullPointerException(prefix + "Null Orientation Controller");
        if (describer == null)
            throw new NullPointerException(prefix + "Null Describer Controller");
        mScaleSpace = scaleSpace;
        mDetector = detector;
        mOrientation = orientation;
        mDescribe = describer;

        // Set the listeners so that anytime any of the controllers are
        // altered then we can automatically update our listeners.
        mScaleSpace.setListener(this);
        mDetector.setListener(this);
        mOrientation.setListener(this);
        mDescribe.setListener(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected DetectDescribePoint<ImageFloat32, SurfFeature> createNewDetectorAndDescriber() {
        return FactoryDetectDescribe.sift(getScaleSpace(),
                getDetector(), getOrientation(), getDescriber());
    }

    public ScaleSpaceController getScaleSpaceController() {
        return mScaleSpace;
    }

    public DetectorController getDetectorController() {
        return mDetector;
    }

    public OrientationController getOrientationController() {
        return mOrientation;
    }

    public DescribeController getDescribeController() {
        return mDescribe;
    }

    /**
     * @return the Sift Scale Space to use, possibly null if none.
     */
    public ConfigSiftScaleSpace getScaleSpace() {
        return (ConfigSiftScaleSpace) mScaleSpace.getConfiguration();
    }

    /**
     * @return the Sift Detector to use, possible null if none.
     */
    public ConfigSiftDetector getDetector() {
        return (ConfigSiftDetector) mDetector.getConfiguration();
    }

    /**
     * @return the Sift Orientation to use, possibly null if default is specified.
     */
    public ConfigSiftOrientation getOrientation() {
        return (ConfigSiftOrientation) mOrientation.getConfiguration();
    }

    /**
     * @return The Sift Describer.
     */
    public ConfigSiftDescribe getDescriber() {
        return (ConfigSiftDescribe) mDescribe.getConfiguration();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Equals and HashCode
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SiftParams)) return false;

        SiftParams that = (SiftParams) o;
        if (!mDescribe.equals(that.mDescribe)) return false;
        if (!mDetector.equals(that.mDetector)) return false;
        if (!mOrientation.equals(that.mOrientation)) return false;
        if (!mScaleSpace.equals(that.mScaleSpace)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = mScaleSpace.hashCode();
        result = 31 * result + mDetector.hashCode();
        result = 31 * result + mOrientation.hashCode();
        result = 31 * result + mDescribe.hashCode();
        return result;
    }
}

package uw.cse.mag.appliancereader.lib.cv.params.surf;

import boofcv.abst.feature.detect.interest.ConfigFastHessian;
import uw.cse.mag.appliancereader.lib.cv.params.core.FeatureDetectionParams;

/**
 * Class that represents general Surf Parameters.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public abstract class SurfParams extends FeatureDetectionParams {

    /**
     * Controller for Fast Hessian.
     * {@link boofcv.abst.feature.detect.interest.ConfigFastHessian} Parameter
     */
    private final FastHessianController mFastHessian;

    /**
     * Creates a base surf parameter.
     *
     * @param fastHessianController Controller for Fast Hessian parameter
     */
    protected SurfParams(FastHessianController fastHessianController) {
        super();

        String prefix = getClass().getSimpleName() + "(), ";
        if (fastHessianController == null)
            throw new NullPointerException(prefix + "Null Fast Hessian Controller");
        mFastHessian = fastHessianController;

        // Set the listener
        mFastHessian.setListener(this);
        checkRep();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////////////////////


    public FastHessianController getFastHessianController() {
        return mFastHessian;
    }

    /**
     * @return Fast Hessian to be used for this feature detector.
     */
    public ConfigFastHessian getFastHessian() {
        return (ConfigFastHessian) mFastHessian.getConfiguration();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Equals and HashCode
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurfParams)) return false;
        if (!super.equals(o)) return false;

        SurfParams that = (SurfParams) o;
        if (!mFastHessian.equals(that.mFastHessian)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + mFastHessian.hashCode();
        return result;
    }
}

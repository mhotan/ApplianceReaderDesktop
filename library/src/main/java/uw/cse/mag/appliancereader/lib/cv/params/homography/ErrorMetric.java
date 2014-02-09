package uw.cse.mag.appliancereader.lib.cv.params.homography;

import boofcv.abst.geo.fitting.DistanceFromModelResidualN;
import boofcv.alg.geo.h.HomographyResidualSampson;
import boofcv.alg.geo.h.HomographyResidualTransfer;
import boofcv.struct.geo.AssociatedPair;
import org.ejml.data.DenseMatrix64F;

/**
 * Created by mhotan_dev on 12/26/13.
 */
public enum ErrorMetric {
    RESIDUAL_SAMPSON, RESIDUAL_TRANSFER;

    public DistanceFromModelResidualN<DenseMatrix64F,AssociatedPair> getMetric() {
        switch (this) {
            case RESIDUAL_SAMPSON:
                return new DistanceFromModelResidualN<DenseMatrix64F, AssociatedPair>(
                        new HomographyResidualSampson());
            case RESIDUAL_TRANSFER:
                return new DistanceFromModelResidualN<DenseMatrix64F, AssociatedPair>(
                        new HomographyResidualTransfer());
            default:
                throw new IllegalStateException(this.getClass().getSimpleName() + ".getMetric() Unsupported " +
                        "Distance from model residual N");
        }
    }
}

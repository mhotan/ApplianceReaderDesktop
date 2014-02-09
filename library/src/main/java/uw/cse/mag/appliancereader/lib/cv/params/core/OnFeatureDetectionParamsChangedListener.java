package uw.cse.mag.appliancereader.lib.cv.params.core;

import boofcv.abst.feature.associate.AssociateDescription;
import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.struct.feature.SurfFeature;
import boofcv.struct.image.ImageFloat32;

/**
 * Interface for any clients to listen for changes in Associaters.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public interface OnFeatureDetectionParamsChangedListener {

    /**
     * Callback to notify the Feature detection and description object changed.
     *
     * @param newVal New Detection and Description mechanism. Not null.
     * @param oldVal Old Detection and Description mechanism. Not null.
     */
    public void onDetDescChanged(DetectDescribePoint<ImageFloat32, SurfFeature> newVal,
                                 DetectDescribePoint<ImageFloat32, SurfFeature> oldVal);

    /**
     * Callback to notify the Descriptor Associater changed.
     *
     * @param newVal New Descriptor associater. Not Null.
     * @param oldVal Old Descriptor associater. Not Null.
     */
    public void onDescriptorAssociaterChanged(AssociateDescription<SurfFeature> newVal,
                                              AssociateDescription<SurfFeature> oldVal);

}

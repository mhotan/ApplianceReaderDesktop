package uw.cse.mag.appliancereader.lib.cv;

import boofcv.abst.feature.associate.AssociateDescription;
import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.alg.feature.UtilFeature;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.struct.FastQueue;
import boofcv.struct.feature.AssociatedIndex;
import boofcv.struct.feature.SurfFeature;
import boofcv.struct.geo.AssociatedPair;
import boofcv.struct.image.ImageFloat32;
import georegression.struct.point.Point2D_F64;
import uw.cse.mag.appliancereader.lib.cv.params.core.FeatureDetectionParams;
import uw.cse.mag.appliancereader.lib.cv.params.core.OnFeatureDetectionParamsChangedListener;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Class that is a wrapper around associating points within images.  This is done by defining an instance of
 * {@link uw.cse.mag.appliancereader.lib.cv.params.core.FeatureDetectionParams} that is used to compute
 * descriptors for each detected feature and associating them together.  This class creates a strong reference
 * to the inputted FeatureDetectionParams.  Every time the parameter instance is changed, the Associater
 * automatically updates all following calls to associate.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class ImagePointAssociator implements OnFeatureDetectionParamsChangedListener {

    /**
     * Strong reference to parameters.
     */
    private final FeatureDetectionParams parameters;

    /**
     * Images to be associated.
     */
    private BufferedImage referenceImage, otherImage;

    /**
     * References to Feature Detection results.
     */
    private DetectionResultArgument referenceImageFeatures, otherImageFeatures;

    /**
     * List of matches between the two images.
     */
    private List<AssociatedPair> matches;

    /**
     * Update listener.
     */
    private Collection<UpdateListener> listeners;

    /**
     * Creates an Image Associater defined by a specific set of
     * feature detection parameters.
     *
     * @param params Parameters that define Associater behavior
     */
    public ImagePointAssociator(FeatureDetectionParams params) {
        if (params == null)
            throw new NullPointerException(getClass().getSimpleName() +
                    "(), Null Feature Detection Parameters");
        this.parameters = params;
        parameters.setListener(this);

        // Create the collection of Listeners.
        listeners = new HashSet<UpdateListener>();
    }

    ///////////////////////////////////////////////////////////////
    ////    Setters
    ///////////////////////////////////////////////////////////////

    /**
     * Sets the reference image for this associator.
     *
     * @param image Image to set as reference image.
     */
    public void setReferenceImage(BufferedImage image) {
        if (image == null) return;
        if (image.equals(referenceImage)) return;
        this.referenceImage = image;
        this.referenceImageFeatures = describeImage(image);
        update();
    }

    /**
     * Sets the other image for this associator.
     *
     * @param image Image to set as other image.
     */
    public void setOtherImage(BufferedImage image) {
        if (image == null) return;
        if (image.equals(otherImage)) return;
        this.otherImage = image;
        this.otherImageFeatures = describeImage(image);
        update();
    }

    ///////////////////////////////////////////////////////////////
    ////    Mutators
    ///////////////////////////////////////////////////////////////

    /**
     * Update the current state.
     */
    private void update() {
        // Make sure we have all both set of features.
        if (referenceImageFeatures == null || otherImageFeatures == null)
            return;
        List<AssociatedPair> oldMatches = matches;
        this.matches = associate(referenceImageFeatures, otherImageFeatures);
        for (UpdateListener listener: listeners)
            listener.onMatchesUpdate(matches, oldMatches);
    }

    /**
     * Detect and associate point features in the two images.  Returns a
     * list of point pairs.  For every p1 and p2 in a pair in the list.  p1 corresponds
     * to the feature in the first image.  p2 corresponds to the same feature in the second image.
     *
     * @param first First image
     * @param second Second image
     * @return List of Associated Pair of points.
     */
    private List<AssociatedPair> associate(
            DetectionResultArgument first,
            DetectionResultArgument second) {

        // Associate features between the two images
        AssociateDescription<SurfFeature> associater =
                this.parameters.getCurrentAssociater();
        associater.setSource(first.getDescriptors());
        associater.setDestination(second.getDescriptors());
        associater.associate();

        // Create the final list of matches between images.
        List<AssociatedPair> matches = new ArrayList<AssociatedPair>();
        FastQueue<AssociatedIndex> matchIndexes = associater.getMatches();
        // Compile the associated points between the images.
        for( int i = 0; i < matchIndexes.size; i++) {
            AssociatedIndex a = matchIndexes.get(i);
            AssociatedPair p = new AssociatedPair(
                    first.getPoints().get(a.src),
                    second.getPoints().get(a.dst));
            matches.add(p);
        }

        // Return the new Associated points.
        return matches;
    }

    ///////////////////////////////////////////////////////////////
    ////    Getters
    ///////////////////////////////////////////////////////////////

    /**
     * Returns Features found for the reference image.
     * <b>Returns null if there is no reference image.</b>
     * @return Reference image features, null if image exists
     */
    public DetectionResultArgument getReferenceImageFeatures() {
        return referenceImageFeatures;
    }

    /**
     * Returns Features found for the other image.
     * <b>Returns null if there is no other image.</b>
     * @return Other image features, null if image exists
     */
    public DetectionResultArgument getOtherImageFeatures() {
        return otherImageFeatures;
    }

    /**
     * Returns the matches between the two images.  Or null if no matches are ready.
     *
     * @return The feature matches between the two images.
     */
    public List<AssociatedPair> getMatches() {
        return matches;
    }

    /**
     * Given an input image.  The aglorithm detects and describes features.
     *
     * @param image The image to detect and describe features.
     * @return The result of the detection process.
     */
    public DetectionResultArgument describeImage(BufferedImage image) {
        if (image == null) return null;
        return describeImage(ConvertBufferedImage.convertFromSingle(
                image, null, ImageFloat32.class));
    }

    /**
     * Given an input image.  The algorithm detects and describes features
     *
     * @param input The image to detect and describe features.
     * @return The result of the detection process.
     */
    public DetectionResultArgument describeImage(ImageFloat32 input) {
        if (input == null) return null;
        // Get the current Detector and Descriptor
        DetectDescribePoint<ImageFloat32, SurfFeature> detDesc = this.parameters.getCurrentDetDesc();

        // Fast Queue that stores the description of detected interest points.
        FastQueue<SurfFeature> descriptors = UtilFeature.createQueue(
                detDesc, this.parameters.getMaxFeatures());
        List<Point2D_F64> points = new ArrayList<Point2D_F64>();

        // Make the call to detect all the features
        detDesc.detect(input);

        // Find all the features.
        for( int i = 0; i < detDesc.getNumberOfFeatures(); i++ ) {
            points.add( detDesc.getLocation(i).copy() );
            descriptors.grow().setTo(detDesc.getDescription(i));

        }
        return new DetectionResultArgument(descriptors, points, input);
    }

    ///////////////////////////////////////////////////////////////
    ////    Listener for changes in state.
    ///////////////////////////////////////////////////////////////

    @Override
    public void onDetDescChanged(DetectDescribePoint<ImageFloat32, SurfFeature> newVal,
                                 DetectDescribePoint<ImageFloat32, SurfFeature> oldVal) {
        if (newVal == null) return;
        this.referenceImageFeatures = describeImage(referenceImage);
        this.otherImageFeatures = describeImage(otherImage);
        update();
    }

    @Override
    public void onDescriptorAssociaterChanged(AssociateDescription<SurfFeature> newVal,
                                              AssociateDescription<SurfFeature> oldVal) {
        if (newVal == null) return;
        update();
    }

    ///////////////////////////////////////////////////////////////
    ////    This classes Interface.
    ///////////////////////////////////////////////////////////////

    /**
     * Sets the listener to notify changes for.
     *
     * @param listener Listener to notify on change
     */
    public void addListener(UpdateListener listener) {
        if (listener == null) return;
        listeners.add(listener);
    }

    /**
     * Attempts to remove listener.
     *
     * @param listener Listener to remove
     * @return Whether the listener was removed.
     */
    public boolean removeListener(UpdateListener listener) {
        return listeners.remove(listener);
    }

    /**
     * Listener for Updates in matches.
     */
    public interface UpdateListener {

        /**
         * Notifies listener that matches were updated.
         *
         * @param matches List of Associate Features Pair.
         */
        public void onMatchesUpdate(List<AssociatedPair> matches,
                                    List<AssociatedPair> oldMatches);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImagePointAssociator)) return false;

        ImagePointAssociator that = (ImagePointAssociator) o;
        if (!parameters.equals(that.parameters)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return parameters.hashCode();
    }

    @Override
    public String toString() {
        return "ImagePointAssociator{" +
                "parameters=" + parameters +
                '}';
    }
}

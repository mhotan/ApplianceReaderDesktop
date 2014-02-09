package uw.cse.mag.appliancereader.lib.cv;

import boofcv.abst.geo.Estimate1ofEpipolar;
import boofcv.struct.geo.AssociatedPair;
import org.ddogleg.fitting.modelset.ModelMatcher;
import org.ejml.data.DenseMatrix64F;
import uw.cse.mag.appliancereader.lib.cv.params.homography.HomographyParams;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Homography Calculator.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class HomographyCalculator implements
        HomographyParams.OnHomographyParametersChangedListener,
        ImagePointAssociator.UpdateListener {

    /**
     * Parameter to solve for homography.
     */
    private final HomographyParams parameters;

    /**
     * Image Point Associator.  This provides the initial list of
     *
     */
    private ImagePointAssociator imageAssociator;

    /**
     * Reference and other image.
     */
    private BufferedImage referenceImage, otherImage;

    /**
     * List of inlier matches.
     */
    private List<AssociatedPair> inliers;

    /**
     * Current Homography matrix.
     */
    private DenseMatrix64F homography;

    /**
     * List of all listeners to be notified when the homography has changed.
     */
    private final List<OnHomographyChangedListener> listeners;

    /**
     * Creates a homography calculator with a strong reference to HomographyParams.
     *
     * @param parameters Specific parameter for Homography Calculations.
     */
    public HomographyCalculator(HomographyParams parameters) {
        if (parameters == null)
            throw new NullPointerException(getClass().getSimpleName() + "() Parameters " +
                    "cannot be null");
        this.parameters = parameters;
        this.parameters.setListener(this);

        this.listeners = new ArrayList<OnHomographyChangedListener>();
    }

    ///////////////////////////////////////////////////////////////
    ////    Setters
    ///////////////////////////////////////////////////////////////

    /**
     * Adds a listener to this for changes in homography.  Changes in homography
     * can be a result from changes in feature detection or homography parameters.
     *
     * @param listener Listeners that wants to here changed homography events.
     */
    public void addListener(OnHomographyChangedListener listener) {
        if (listener == null) return;
        listeners.add(listener);
    }

    /**
     * Set the image associator.
     *
     * @param imageAssociator Image Associator for obtaining matches.
     */
    public void setImageAssociator(ImagePointAssociator imageAssociator) {
        // Ignore Null associators
        if (imageAssociator == null) return;

        // Deactivate this
        if (this.imageAssociator != null)
            this.imageAssociator.removeListener(this);

        // Update with the new Image associator.
        this.imageAssociator = imageAssociator;
        this.imageAssociator.addListener(this);

        // Attempt to set the reference image.
        if (referenceImage != null) {
            this.imageAssociator.setReferenceImage(referenceImage);
        }
        // Attempt to set the other image.
        if (otherImage != null) {
            this.imageAssociator.setOtherImage(otherImage);
        }
    }

    /**
     * Sets the reference Image of the calculator.
     *
     * @param referenceImg Set the reference Image.
     * @throws java.lang.NullPointerException when the argument image is null
     */
    public void setReferenceImage(BufferedImage referenceImg) {
        if (referenceImg == null) {
            throw new NullPointerException(getClass().getSimpleName() + "setReferenceImage(), " +
                    "Image cannot be null");
        }
        // Stop if this image is the current image.
        if (referenceImg.equals(this.referenceImage)) return;
        this.referenceImage = referenceImg;

        // If the image associator is set.
        if (this.imageAssociator != null)
            this.imageAssociator.setReferenceImage(this.referenceImage);
    }

    /**
     * Sets the other image to find the homography for.
     *
     * @param otherImg Set the other Image.
     * @throws java.lang.NullPointerException when the argument image is null
     */
    public void setOtherImage(BufferedImage otherImg) {
        if (otherImg == null) {
            throw new NullPointerException(getClass().getSimpleName() + "setOtherImage(), " +
                    "Image cannot be null");
        }
        if (otherImg.equals(this.otherImage)) return;
        this.otherImage = otherImg;

        // If the image associator is set.
        if (this.imageAssociator != null)
            this.imageAssociator.setOtherImage(this.otherImage);
    }


    ///////////////////////////////////////////////////////////////
    ////    Mutators
    ///////////////////////////////////////////////////////////////

    /**
     * Removes the listener from this.
     *
     * @param listener Listener to attempt to remove
     * @return Whether or not this had the listener.
     */
    public boolean removeListener(OnHomographyChangedListener listener) {
        return listeners.remove(listener);
    }

    /**
     * Computes the inlier matches via RANSAC.  It uses all the matches and runs the algorithm.
     *
     * @param matches All matches between points.
     * @return Null if unable to compute inliers, List of inlier matches.
     */
    private List<AssociatedPair> computeInliers(List<AssociatedPair> matches) {
        if (matches == null)
            throw new NullPointerException(getClass().getSimpleName() +
                    ".computeInliers() Matches cannot be null");
        if (matches.isEmpty()) return null;

        // Using RANSAC to estimate the homography.
        ModelMatcher<DenseMatrix64F, AssociatedPair> robustHomography = parameters.getModelMatcher();
        if (!robustHomography.process(matches)) return null;

        // Return the list of inlier matches.
        inliers = new ArrayList<AssociatedPair>(robustHomography.getMatchSet());
        return inliers;
    }

    /**
     * Computes the homography using the inlier matches.
     *
     * @return The Homography Matrix.
     */
    private DenseMatrix64F computeHomography(List<AssociatedPair> inliers) {
        if (inliers == null) {
            throw new IllegalStateException(getClass().getSimpleName() + ".computeHomography(), " +
                    "Inliers cannot be null.");
        }
        if (inliers.size() < 4) {
            // Not enough matches.
            return null;
        }

        Estimate1ofEpipolar computeHomography = parameters.getHomographyComputer();
        DenseMatrix64F H = new DenseMatrix64F(3, 3);
        computeHomography.process(inliers, H);
        DenseMatrix64F oldHomo = this.homography;
        homography = H;

       // Notify listeners for new homography updates.
        for (OnHomographyChangedListener listener: listeners)
            listener.onHomographyChanged(homography, oldHomo);
        return homography;
    }

    ///////////////////////////////////////////////////////////////
    ////    Getters
    ///////////////////////////////////////////////////////////////

    /**
     * Based off how the images are described and matches are found, filtered, and processed.
     * Return the inlier matches between images.
     *
     * @return Inlier matches.
     */
    public List<AssociatedPair> getInliers() {
        return inliers;
    }

    /**
     * Based off how the images are associated, return the homography matrix
     *
     * @return The homography matrix between images.
     */
    public DenseMatrix64F getHomography() {
        return homography;
    }

    /**
     * Returns the image associator between images.
     *
     * @return Current Image Associator.
     */
    public ImagePointAssociator getImageAssociator() {
        return imageAssociator;
    }

    /**
     * Returns a reference to the reference image.
     *
     * @return The Reference image.
     */
    public BufferedImage getReferenceImage() {
        return referenceImage;
    }

    /**
     * Returns a reference to the other image.
     *
     * @return The Other image.
     */
    public BufferedImage getOtherImage() {
        return otherImage;
    }

    ///////////////////////////////////////////////////////////////
    ////    Listener for changes in state.
    ///////////////////////////////////////////////////////////////

    @Override
    public void onHomographyComputerChanged(Estimate1ofEpipolar newVal, Estimate1ofEpipolar oldVal) {
        if (inliers == null || inliers.isEmpty()) return;
        computeHomography(inliers);
    }

    @Override
    public void onModelMatcherChanged(ModelMatcher<DenseMatrix64F, AssociatedPair> newVal, ModelMatcher<DenseMatrix64F, AssociatedPair> oldVal) {
        if (imageAssociator == null) return;
        List<AssociatedPair> matches = imageAssociator.getMatches();
        if (matches == null || matches.isEmpty()) return;

        // Compute the inlier matches
        computeHomography(computeInliers(matches));
    }

    @Override
    public void onMatchesUpdate(List<AssociatedPair> matches, List<AssociatedPair> oldMatches) {
        if (matches == null)
            throw new IllegalStateException(getClass().getSimpleName() + "onMatchesUpdate(), " +
                    "Can't have null matches.");
        computeHomography(computeInliers(matches));
    }

    /////////////////////////////////////////////////////////////////
    ////    Interface for notification callbacks
    /////////////////////////////////////////////////////////////////

    /**
     * Interface for listening in changing
     */
    public interface OnHomographyChangedListener {

        /**
         * Notification that the homography matrix has changed.
         *
         * @param newHomography The new homography.
         * @param oldHomography The old homography.
         */
        public void onHomographyChanged(DenseMatrix64F newHomography, DenseMatrix64F oldHomography);

    }

}

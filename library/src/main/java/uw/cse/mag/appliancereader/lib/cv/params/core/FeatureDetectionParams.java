package uw.cse.mag.appliancereader.lib.cv.params.core;

import boofcv.abst.feature.associate.AssociateDescription;
import boofcv.abst.feature.associate.ScoreAssociation;
import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.factory.feature.associate.FactoryAssociation;
import boofcv.struct.Configuration;
import boofcv.struct.feature.SurfFeature;
import boofcv.struct.image.ImageFloat32;

/**
 * Parameters that define how to Associate Points on corresponding images.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public abstract class FeatureDetectionParams implements OnConfigurationChangedListener {

    // Default Values.
    public static final Scorer DEFAULT_SCORER = Scorer.DEFAULT;
    public static final boolean DEFAULT_SQUARE_EUCLIDEAN = false;
    public static final boolean DEFAULT_BACKWARDS_VALIDATION = true;
    public static final double DEFAULT_MAX_DISTANCE = Double.MAX_VALUE;
    public static final int DEFAULT_MAX_FEATURES = 100;
    public static final Class IMAGE_TYPE = ImageFloat32.class;

    /**
     * {@link Scorer} Parameter
     * <b>Helps create </b>
     */
    private Scorer scorer;

    /**
     * <b>Square Euclidian distance for ScoreAssociation</b>
     * <b>Backwards validation for Associate Description.</b>
     */
    private boolean squareEuclidian, backwardsValidation;

    /**
     * Maximum distance between features.
     */
    private double maxDistance;

    /**
     * Maximum number of total features to associate between points.
     */
    private int maxFeatures;

    /**
     * See {@link OnFeatureDetectionParamsChangedListener} for more information.
     * <b>Listener for changes in Feature Detection parameter changes.</b>
     */
    private OnFeatureDetectionParamsChangedListener listener;

    /**
     * Current detector and descriptor.
     */
    private DetectDescribePoint<ImageFloat32, SurfFeature> currentDetDesc;

    /**
     * Current descriptor associater.
     */
    private AssociateDescription<SurfFeature> currentAssociater;

    /**
     * Creates a Default Parameter options for Feature Detection Parameter.
     */
    protected FeatureDetectionParams() {
        this.scorer = DEFAULT_SCORER;
        this.squareEuclidian = DEFAULT_SQUARE_EUCLIDEAN;
        this.backwardsValidation = DEFAULT_BACKWARDS_VALIDATION;
        this.maxDistance = DEFAULT_MAX_DISTANCE;
        this.maxFeatures = DEFAULT_MAX_FEATURES;
        currentDetDesc = null;
    }

    /**
     * Creates a new Detector and Describer with the the current state of this.
     * <b>Guaranteed to be called after construction.</b>
     *
     * @return New Detector and Describer.
     */
    protected abstract DetectDescribePoint<ImageFloat32, SurfFeature> createNewDetectorAndDescriber();

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Mutators
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * Update the detector with the current state and notifies listeners of changed detector.
     * <b>Called anytime when the state of this changes.</b>
     */
    protected void updateDetector() {
        DetectDescribePoint<ImageFloat32, SurfFeature> oldDetDesc = currentDetDesc;
        currentDetDesc = createNewDetectorAndDescriber();
        if (listener != null)
            listener.onDetDescChanged(currentDetDesc, oldDetDesc);
        checkRep();
    }

    /**
     * Update the Feature Associater with the current state and notifies listeners of changed Associater.
     * <b>Called anytime the state of the Associater should be changed.</b>
     */
    protected void updateAssociater() {
        AssociateDescription<SurfFeature> oldAssociater = currentAssociater;
        currentAssociater = FactoryAssociation.greedy(
                getScorer(), getMaxDistance(), isBackwardsValidated());
        if (this.listener != null)
            listener.onDescriptorAssociaterChanged(currentAssociater, oldAssociater);
        checkRep();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * Surf Features need a way to be able to assess similarities between them.  A scorer provides
     * scores based on qualities between Surf Features.  Lower scores are better.
     * <b>Runtime: O(1)</b>
     *
     * @return An object that can score fit quality between Surf Feature Descriptions
     */
    protected ScoreAssociation<SurfFeature> getScorer() {
        DetectDescribePoint<ImageFloat32, SurfFeature> detector = getCurrentDetDesc();
        switch (this.scorer) {
            case DEFAULT:
                return FactoryAssociation.defaultScore(detector.getDescriptionType());
            case EUCLIDEAN:
                return FactoryAssociation.scoreEuclidean(detector.getDescriptionType(),
                        isSquareEuclidian());
            case HAMMING:
                return FactoryAssociation.scoreHamming(detector.getDescriptionType());
            case SAD:
                return FactoryAssociation.scoreSad(detector.getDescriptionType());
        }
        throw new IllegalStateException(getClass().getSimpleName() + ".getScorer() Unsupported " +
                "\"" + scorer + "\"");
    }

    /**
     *
     * @return
     */
    public Scorer getScorerEnum() {
         return this.scorer;
    }

//    /**
//     * Provides the associater between different features.  The Associater is based
//     * on the scorer, maximum distance between features, and whether backwards validation is
//     * requested.
//     *
//     * @return The Associator used for associating feature descriptions
//     */
//    public AssociateDescription<SurfFeature> getAssociator() {
//        return FactoryAssociation.greedy(getScorer(), getMaxDistance(), isBackwardsValidated());
//    }

    /**
     * @return The maximum distance for errors.
     */
    public double getMaxDistance() {
        return this.maxDistance;
    }

    /**
     * Defines whether this instance wants to square euclidian distances for a givien scorer.
     *
     * @return Whether or not this instance desired to square Euclidian distances.
     */
    public boolean isSquareEuclidian() {
        return squareEuclidian;
    }

    /**
     * Checks whether or we want to ensure feature matching does forward and backwards validation.
     *
     * @return Whether or not to backward Validate
     */
    public boolean isBackwardsValidated() {
        return backwardsValidation;
    }

    /**
     * @return The maximum number of features for comparisons.
     */
    public int getMaxFeatures() {
        return maxFeatures;
    }

    /**
     * Provide a feature detector and descriptor with the current state of this parameter.
     *
     * @return Current Detector and Descriptor.
     */
    public DetectDescribePoint<ImageFloat32, SurfFeature> getCurrentDetDesc() {
        // First time initializing feature detector and descriptor.
        if (currentDetDesc == null) {
            // Initialize the detector and descriptor.
            currentDetDesc = createNewDetectorAndDescriber();
            assert currentDetDesc != null: "updateDetector() " +
                    "Failed to initialize Detector and Descriptor.";
        }
        return currentDetDesc;
    }

    /**
     * Provides a feature associater with the current state of this.
     *
     * @return Feature Associater.
     */
    public AssociateDescription<SurfFeature> getCurrentAssociater() {
        // First time initialize feature associater.
        if (currentAssociater == null) {
            // Initialize the associater.
            currentAssociater = FactoryAssociation.greedy(
                    getScorer(), getMaxDistance(), isBackwardsValidated());
            assert currentAssociater != null: "updateAssociater() " +
                    "Failed to initialize the Detector and Descriptor.";
        }
        return currentAssociater;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Setters
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * Assign the listener that listens for changes in state.
     *
     * @param listener Listener to assign. Null to clear out any listeners.
     */
    public void setListener(OnFeatureDetectionParamsChangedListener listener) {
        this.listener = listener;
    }

    /**
     * Sets the type of score to use for this Feature Detection Parameter instance.
     *
     * @param scorer The new feature scorer to use
     */
    public void setScorer(Scorer scorer) {
        if (scorer == null)
            throw new NullPointerException(getClass().getSimpleName() + ".getScorer() " +
                    "Null Scorer argument");
        if (scorer.equals(this.scorer)) return;
        this.scorer = scorer;
        updateAssociater();
    }

    /**
     * If the scorer is chosen to be Euclidian distance.  Clients are allowed to
     * have the Euclidian distances be more discriminate by squaring the values.
     *
     * @param squareEuclidian Boolean flag whether to square Euclidian value.
     */
    public void setSquareEuclidian(boolean squareEuclidian) {
        if (this.squareEuclidian == squareEuclidian) return;
        this.squareEuclidian = squareEuclidian;
        updateAssociater();
    }

    /**
     * If there is a requirement for forward and backwards matches.  Then use this function
     * to set the value of this backward validation.
     *
     * @param backwardsValidation whether or not backwards validation will be used.
     */
    public void setBackwardsValidation(boolean backwardsValidation) {
        if (this.backwardsValidation == backwardsValidation) return;
        this.backwardsValidation = backwardsValidation;
        updateAssociater();
    }

    /**
     * Set the maximum distance between features. Maximum allowed error between features.
     * To disable set to Double.MAX_VALUE.
     * <b>Cannot assign Max value to be negative.</b>
     *
     * @param maxDistance Maximum distance between feature points.
     */
    public void setMaxDistance(double maxDistance) {
        if (maxDistance <= 0.0)
            throw new IllegalArgumentException(getClass().getSimpleName() + ".setMaxDistance()" +
                    " Can't have negative maximum distance.");
        if (Double.compare(this.maxDistance, maxDistance) == 0) return;
        this.maxDistance = maxDistance;
        updateAssociater();
    }

    /**
     *
     *
     * @param maxFeatures
     */
    public void setMaxFeatures(int maxFeatures) {
        if (maxFeatures <= 0)
            throw new IllegalArgumentException(getClass().getSimpleName() + ".setMaxFeatures()" +
            " Can't have nonpositive maximum amount of features.");
        if (this.maxFeatures == maxFeatures) return;
        this.maxFeatures = maxFeatures;
        updateAssociater();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Implemented Interfaces
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onConfigurationChanged(Configuration newVal,
                                       Configuration oldVal,
                                       ConfigurationType type) {
        // Regardless of with configuration was changed,
        // Update the Feature detector.
        updateDetector();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Equals and HashCode
    ////////////////////////////////////////////////////////////////////////////////////


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeatureDetectionParams)) return false;

        FeatureDetectionParams that = (FeatureDetectionParams) o;
        if (backwardsValidation != that.backwardsValidation) return false;
        if (Double.compare(that.maxDistance, maxDistance) != 0) return false;
        if (maxFeatures != that.maxFeatures) return false;
        if (squareEuclidian != that.squareEuclidian) return false;
        if (scorer != that.scorer) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = scorer.hashCode();
        result = 31 * result + (squareEuclidian ? 1 : 0);
        result = 31 * result + (backwardsValidation ? 1 : 0);
        temp = Double.doubleToLongBits(maxDistance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + maxFeatures;
        return result;
    }

    /**
     * Representation Checker.
     */
    protected void checkRep() {
        String prefix = this.getClass().getSimpleName() + ".checkRep() ";
        assert this.scorer != null: prefix + "Can't have null Scorer";
        assert this.maxDistance > 0.0: prefix + "Can't have non positive maximum distance";
        assert this.maxFeatures > 0: prefix + "Can't have non positive maximum features";
    }

}

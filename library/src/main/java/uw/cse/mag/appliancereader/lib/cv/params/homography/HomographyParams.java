package uw.cse.mag.appliancereader.lib.cv.params.homography;

import boofcv.abst.geo.Estimate1ofEpipolar;
import boofcv.abst.geo.fitting.DistanceFromModelResidualN;
import boofcv.abst.geo.fitting.GenerateEpipolarMatrix;
import boofcv.factory.geo.FactoryMultiView;
import boofcv.struct.geo.AssociatedPair;
import org.ddogleg.fitting.modelset.ModelMatcher;
import org.ddogleg.fitting.modelset.ransac.Ransac;
import org.ejml.data.DenseMatrix64F;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;

/**
 * Parameters for Homography transformation.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class HomographyParams {

    // Default Values
    public static final boolean DEFAULT_NORMALIZE = true;
    public static final ErrorMetric DEFAULT_ERROR_METRIC = ErrorMetric.RESIDUAL_SAMPSON;
    public static final long DEFAULT_RANDOMIZE_SEED = 123123;
    public static final int DEFAULT_NUM_ITERATIONS = 1000;
    public static final double DEFAULT_THRESHOLD_FIT = 1.0;

    /**
     * Flag that annotates whether the input needs to be normalized.
     * <b>If the input is in pixel coordinates then it should be normalized. Set value to true.</b>
     * <b>If the input is already normalized then set this to false.</b>
     */
    private boolean normalize;

    /**
     * How to compute differences between the point projected by the homography and its observed location.
     */
    private ErrorMetric errorMetric;

    /**
     * Random seed used by the Random number generator.
     */
    private long randomizeSeed;

    /**
     * The maximum number of iterations the RANSAC algorithm will perform.
     * <b>Unit of measurement is in ones.</b>
     */
    private int numIterations;

    /**
     * How close of a fit two points need to be the model to be considered a fit.
     * <b>Unit is in Pixels.</b>
     */
    private double thresholdFit;

    /**
     * Model Matcher for finding inlier matches.
     */
    private ModelMatcher<DenseMatrix64F, AssociatedPair> modelMatcher;

    /**
     * Homography computer.
     */
    private Estimate1ofEpipolar homographyComputer;

    /**
     * Listener for changes in state.
     */
    private OnHomographyParametersChangedListener listener;

    /**
     * Creates default Homography Parameters.
     */
    public HomographyParams() {
        this(DEFAULT_NORMALIZE, DEFAULT_ERROR_METRIC,
                DEFAULT_RANDOMIZE_SEED, DEFAULT_NUM_ITERATIONS, DEFAULT_THRESHOLD_FIT);
    }

    /**
     * Create Homography Parameters.
     *
     * @param normalize Whether to normalize points
     * @param errorMetric Way to measure errors.
     * @param randomizeSeed Randomize seed
     * @param numIterations Number of iterations (in thousands)
     * @param thresholdFit Threshold for the fit.
     */
    public HomographyParams(boolean normalize,
                            ErrorMetric errorMetric,
                            long randomizeSeed,
                            int numIterations,
                            double thresholdFit) {
        this.normalize = normalize;
        this.errorMetric = errorMetric;
        this.randomizeSeed = randomizeSeed;
        this.numIterations = numIterations;
        this.thresholdFit = thresholdFit;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Mutators
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * Estimate the homography computer with current state.
     */
    private void updateHomographyComputer() {
        Estimate1ofEpipolar oldVal = homographyComputer;
        homographyComputer = getHomographyComputer();
        if (this.listener != null)
            listener.onHomographyComputerChanged(homographyComputer, oldVal);
        updateModelMatcher();
    }

    /**
     * Updates the model matcher with the most current parameters.
     */
    private void updateModelMatcher() {
        ModelMatcher<DenseMatrix64F, AssociatedPair> oldVal = modelMatcher;
        modelMatcher = getModelMatcher();
        if (this.listener != null)
            listener.onModelMatcherChanged(modelMatcher, oldVal);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Setters
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * If the input will be in pixel coordinates set to true.
     * <b>Otherwise if already normalized, set to false</b>
     *
     * @param normalize Flag to notify algorithm to normal input.
     */
    public void setNormalize(boolean normalize) {
        if (this.normalize == normalize) return; // No need to update.
        this.normalize = normalize;
        updateHomographyComputer();
    }

    /**
     * Set the error metric for this homography.
     *
     * @param errorMetric The type of error measuring.
     */
    public void setErrorMetric(ErrorMetric errorMetric) {
        if (errorMetric == null)
            throw new IllegalArgumentException(getClass().getSimpleName() + "setErrorMetric(), Null input.");
        if (this.errorMetric.equals(errorMetric)) return;
        this.errorMetric = errorMetric;
        updateModelMatcher();
    }

    /**
     * Set the randomization seed for the Ransac algorithm.  This number
     * is the seed for the random number generator.
     *
     * @param randomizeSeed Randomization seed.
     */
    public void setRandomizeSeed(long randomizeSeed) throws ConfigException {
        if (randomizeSeed <= 0)
            throw new ConfigException("Randomized seed must be positive!");
        if (this.randomizeSeed == randomizeSeed) return;
        this.randomizeSeed = randomizeSeed;
        updateModelMatcher();
    }

    /**
     * Set the number of iterations Ransac will limit itself to.
     *
     * @param numIterations Number of iterations
     */
    public void setNumIterations(int numIterations) throws ConfigException {
        if (numIterations <= 0)
            throw new ConfigException("Number of iterations must be positive!");
        if (this.numIterations == numIterations) return;
        this.numIterations = numIterations;
        updateModelMatcher();
    }

    /**
     * Set the threshold of acceptance for seeing how close a point needs to
     * be to be considered a fit.
     *
     * @param thresholdFit Threshold in pixels.
     */
    public void setThresholdFit(double thresholdFit) throws ConfigException {
        if (thresholdFit <= 0.0)
            throw new ConfigException("Fit threshold for Ransac must be positive");
        if (Double.compare(this.thresholdFit, thresholdFit) == 0) return;
        this.thresholdFit = thresholdFit;
        updateModelMatcher();
    }

    public void setListener(OnHomographyParametersChangedListener listener) {
        this.listener = listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return Whether the input needs to be normalized.
     */
    public boolean isNormalize() {
        return normalize;
    }

    /**
     * Provides the error metric for measuring the accuracy of the matrix.
     *
     * @return The error metric for calculating erros.
     */
    public ErrorMetric getErrorMetric() {
        return errorMetric;
    }

    /**
     * Provides the randomization seed.  This seed will be used by the random number
     * generation.
     *
     * @return The randomization seed.
     */
    public long getRandomizeSeed() {
        return randomizeSeed;
    }

    /**
     * The maximum number of iterations the RANSAC algorithm will perform.
     *
     * @return Number of iterations to use in Ransac.
     */
    public int getNumIterations() {
        return numIterations;
    }

    /**
     * How close of a fit a points needs to be to the model to be considered a fit. In pixels
     *
     * @return Number of pixels.
     */
    public double getThresholdFit() {
        return thresholdFit;
    }

    /**
     * Provides a Model Matcher for gathering inliers.
     *
     * @return The model matcher for processing inliers.
     */
    public ModelMatcher<DenseMatrix64F, AssociatedPair> getModelMatcher() {
        if (modelMatcher == null) {
            modelMatcher = createMatcher();
        }
        return modelMatcher;
    }

    /**
     * @return The element that can compute the homography matrix.
     */
    public Estimate1ofEpipolar getHomographyComputer() {
        if (homographyComputer == null) {
            homographyComputer = FactoryMultiView.computeHomography(isNormalize());
        }
        return homographyComputer;
    }

    /**
     * Creates Ransac model matcher.
     * @return The model matcher of this.
     */
    private ModelMatcher createMatcher() {
        // Select which linear algorithm is to be used.
        // Currently we will use Homography to match different perspective views.
        Estimate1ofEpipolar computeHomography = getHomographyComputer();

        // Wrapper so that this estimator can be used by the robust estimator
        GenerateEpipolarMatrix generateH = new GenerateEpipolarMatrix(computeHomography);

        // How the error is Measured
        // Tailored to use
        DistanceFromModelResidualN<DenseMatrix64F, AssociatedPair> errorMetric = getErrorMetric().getMetric();

        // Return the Ransac
        return new Ransac<DenseMatrix64F, AssociatedPair>(getRandomizeSeed(), generateH, errorMetric,
                getNumIterations(), getThresholdFit());
    }

    /**
     * @return Random value between 0 and Long.MAX_VALUE
     */
    private static long generateRandom() {
        return (long) Math.random() * Long.MAX_VALUE;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Equals and HashCode
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HomographyParams)) return false;

        HomographyParams that = (HomographyParams) o;
        if (normalize != that.normalize) return false;
        if (numIterations != that.numIterations) return false;
        if (randomizeSeed != that.randomizeSeed) return false;
        if (Double.compare(that.thresholdFit, thresholdFit) != 0) return false;
        if (errorMetric != that.errorMetric) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (normalize ? 1 : 0);
        result = 31 * result + errorMetric.hashCode();
        result = 31 * result + (int) (randomizeSeed ^ (randomizeSeed >>> 32));
        result = 31 * result + numIterations;
        temp = Double.doubleToLongBits(thresholdFit);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "HomographyParams{" +
                "thresholdFit=" + thresholdFit +
                ", numIterations=" + numIterations +
                ", randomizeSeed=" + randomizeSeed +
                ", errorMetric=" + errorMetric +
                ", normalize=" + normalize +
                '}';
    }

    /**
     * Listener for changes in state.
     */
    public interface OnHomographyParametersChangedListener {

        /**
         * Callback to notify clients that the homography computer has been updated.
         *
         * @param newVal New Homography computer, Will not be null
         * @param oldVal Old Homography computer, May be null.
         */
        public void onHomographyComputerChanged(Estimate1ofEpipolar newVal,
                                                Estimate1ofEpipolar oldVal);

        /**
         * Callback to notify clients that the model matcher has been updated.
         *
         * @param newVal New Model Matcher, Will not be null.
         * @param oldVal Old Model Matcher, May be null.
         */
        public void onModelMatcherChanged(ModelMatcher<DenseMatrix64F, AssociatedPair> newVal,
                                          ModelMatcher<DenseMatrix64F, AssociatedPair> oldVal);

    }
}

package uw.cse.mag.appliancereader.lib.cv.params.sift;

import boofcv.abst.feature.describe.ConfigSiftDescribe;
import uw.cse.mag.appliancereader.lib.cv.params.core.BaseController;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigurationType;
import uw.cse.mag.appliancereader.lib.cv.params.core.OnConfigurationChangedListener;

/**
 * Controller class for adjusting Sift Describer.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class DescribeController extends BaseController {

    // Default Values.
    public static final int DEFAULT_GRID_WIDTH = 4;
    public static final int DEFAULT_NUM_SAMPLES = 8;
    public static final int DEFAULT_NUM_HIST_BINS = 8;
    public static final double DEFAULT_WEIGHT_SIGMA = .5;
    public static final double DEFAULT_SIGMA_TO_RADIUS = 3;

    /**˜                                                                           ˜
     * Number of grid elements along a side.
     * <b>Number of samples along a grid.</b>
     * <b>Number of bins in the orientation histogram.</b>
     *
     * See {@link boofcv.abst.feature.describe.ConfigSiftDescribe} for more information.
     */
    private int gridWidth, numSamples, histBins;

    /**
     * Descriptor element's weighting from center
     * <b>Scale space to pixels.</b>
     */
    private double weightSigma, sigmaToRadius;

    public DescribeController() {
        this(null);
    }

    public DescribeController(OnConfigurationChangedListener listener) {
        super(listener);
        gridWidth = DEFAULT_GRID_WIDTH;
        numSamples = DEFAULT_NUM_SAMPLES;
        histBins = DEFAULT_NUM_HIST_BINS;
        weightSigma = DEFAULT_WEIGHT_SIGMA;
        sigmaToRadius = DEFAULT_SIGMA_TO_RADIUS;
        try {
            update();
        } catch (ConfigException e) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " Illegal default fields");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Mutator and Setters
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * Adjust the number of grid elements along a side.
     * <b>Typically 4</b>
     *
     * @param gridWidth Number of grid elements alond a side.
     * @throws ConfigException Invalid argument
     */
    public void setGridWidth(int gridWidth) throws ConfigException {
        if (this.gridWidth == gridWidth) return;
        this.gridWidth = gridWidth;
        update();
    }

    /**
     * Adjust the number of samples along a grid.
     * <b>Typically 8</b>
     *
     * @param numSamples Number of samples along a grid.
     * @throws ConfigException Invalid argument
     */
    public void setNumSamples(int numSamples) throws ConfigException {
        if (this.numSamples == numSamples) return;
        this.numSamples = numSamples;
        update();
    }

    /**
     * Adjust number of bins in the orientation histogram.
     * <b>Typically 8</b>
     *
     * @param numHistBins Number of bins in the orientation histogram.
     * @throws ConfigException Invalid argument
     */
    public void setNumHistBins(int numHistBins) throws ConfigException {
        if (histBins == numHistBins) return;
        this.histBins = numHistBins;
        update();
    }

    /**
     * Adjusts descriptor element's weighting from center.
     * <b>Typically 0.5</b>
     *
     * @param weightSigma descriptor elements weighting from center.
     * @throws ConfigException Invalid argument
     */
    public void setWeightSigma(double weightSigma) throws ConfigException {
        if (this.weightSigma == weightSigma) return;
        this.weightSigma = weightSigma;
        update();
    }

    /**
     * Conversation from scale space to pixels.
     * <b>Typically 3</b>
     * @param sigmaToRadius Scale space to pixels.
     */
    public void setSigmaToRadius(double sigmaToRadius) throws ConfigException {
        if (this.sigmaToRadius == sigmaToRadius) return;
        this.sigmaToRadius = sigmaToRadius;
        update();
    }


    ////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////////////////////


    public int getGridWidth() {
        return gridWidth;
    }

    public int getNumSamples() {
        return numSamples;
    }

    public int getHistBins() {
        return histBins;
    }

    public double getWeightSigma() {
        return weightSigma;
    }

    public double getSigmaToRadius() {
        return sigmaToRadius;
    }

    @Override
    protected ConfigSiftDescribe getCurrentConfiguration() {
        return new ConfigSiftDescribe(gridWidth, numSamples,
                histBins, weightSigma, sigmaToRadius);
    }

    @Override
    public ConfigurationType getConfigurationType() {
        return ConfigurationType.SIFT_DESCRIBE;
    }
}

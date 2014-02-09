package application.uw.cse.mag.appliancereader.views.input.params.features;

import application.uw.cse.mag.appliancereader.views.input.DoubleInputView;
import application.uw.cse.mag.appliancereader.views.input.IntegerInputView;
import application.uw.cse.mag.appliancereader.views.input.NumberInputView;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.surf.AverageIntegralController;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class AverageIntegralView extends ConfigView {

    private final AverageIntegralController controller;

    private static final String RADIUS = "Radius";
    private static final String SAMPLE_WIDTH = "Sample Width";
    private static final String SAMPLE_PERIOD = "Sample Period";
    private static final String WEIGHT_SIGMA = "Weight Sigma";

    public AverageIntegralView(AverageIntegralController controller) {
        super("Average Integral", controller);
        if (controller == null) throw new NullPointerException();
        this.controller = controller;
        initializeControls();
    }

    @Override
    protected void initializeControls() {

        // Radius
        IntegerInputView radiusView = new IntegerInputView(
                RADIUS,
                controller.getRadius(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setRadius(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'" + RADIUS + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        radiusView.setToolTip("Radius of the region being considered in terms of samples. Typically 6.");
        addInputView(radiusView);

        // Sample Width
        IntegerInputView sampleWidthView = new IntegerInputView(
                SAMPLE_WIDTH,
                controller.getSampleWidth(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setSampleWidth(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'" + SAMPLE_WIDTH + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        sampleWidthView.setToolTip("Adjust How often the image is sampled. " +
                "This number is scaled. Typically 1.");
        addInputView(sampleWidthView);

        // Sample Period
        DoubleInputView samplePeriodView = new DoubleInputView(
                SAMPLE_PERIOD,
                controller.getSamplePeriod(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setSamplePeriod(value.doubleValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'" + SAMPLE_PERIOD + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        samplePeriodView.setToolTip("How wide of a kernel should be used to sample. \nTry 6");
        addInputView(samplePeriodView);

        // Weight Sigma
        DoubleInputView weightSigmaView = new DoubleInputView(
                WEIGHT_SIGMA,
                controller.getWeightSigma(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setWeightSigma(value.doubleValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'" + WEIGHT_SIGMA + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        weightSigmaView.setToolTip("Sigma for weighting. zero for unweighted. " +
                "less than zero for automatic. \nTry -1.");
        addInputView(weightSigmaView);

    }
}

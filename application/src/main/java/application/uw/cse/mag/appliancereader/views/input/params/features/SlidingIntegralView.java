package application.uw.cse.mag.appliancereader.views.input.params.features;

import application.uw.cse.mag.appliancereader.views.input.DoubleInputView;
import application.uw.cse.mag.appliancereader.views.input.IntegerInputView;
import application.uw.cse.mag.appliancereader.views.input.NumberInputView;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.surf.SlidingIntegralController;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class SlidingIntegralView extends ConfigView {

    private static final String RADIUS = "Radius";
    private static final String SAMPLE_WIDTH = "Sample Width";
    private static final String SAMPLE_PERIOD = "Sample Period";
    private static final String WINDOW_SIZE = "Window Size";
    private static final String WEIGHT_SIGMA = "Weight Sigma";

    private final SlidingIntegralController controller;

    public SlidingIntegralView(SlidingIntegralController controller) {
        super("Sliding Integral", controller);
        if (controller == null)
            throw new NullPointerException();
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
                }
        );
        radiusView.setToolTip("Adjust the radius of the region being " +
                "considered in terms of samples.\n" +
                "Typically 8");
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
                }
        );
        sampleWidthView.setToolTip("Adjust the size of the kernel doing the sampling.\n" +
                "Typically 6.");
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
                }
        );
        samplePeriodView.setToolTip("Adjust how often the image is sampled.  " +
                "This number is scaled.\n" +
                "Typically 0.65");
        addInputView(samplePeriodView);

        // Window Size
        DoubleInputView sampleWindowSize = new DoubleInputView(
                WINDOW_SIZE,
                controller.getWindowSize(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setWindowSize(value.doubleValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'" + WINDOW_SIZE + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                }
        );
        sampleWindowSize.setToolTip("Adjust the size of the angular window.\n" +
                "Try Math.PI / 3");
        addInputView(sampleWindowSize);

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
                }
        );
        weightSigmaView.setToolTip("Adjust the sigma for weighting distribution.\n" +
                "Use 0 for unweighted\n" +
                "Less than zero for automatic");
        addInputView(weightSigmaView);

    }
}

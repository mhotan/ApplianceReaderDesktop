package application.uw.cse.mag.appliancereader.views.input.params.features;

import application.uw.cse.mag.appliancereader.views.input.DoubleInputView;
import application.uw.cse.mag.appliancereader.views.input.FloatInputView;
import application.uw.cse.mag.appliancereader.views.input.IntegerInputView;
import application.uw.cse.mag.appliancereader.views.input.NumberInputView;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.sift.DetectorController;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class SiftDetectorView extends ConfigView {

    private final DetectorController controller;

    public SiftDetectorView(DetectorController controller) {
        super("Sift Detector", controller);
        if (controller == null) throw new NullPointerException("Null Controller");
        this.controller = controller;
        initializeControls();
    }

    @Override
    protected void initializeControls() {

        // Detection Threshold
        FloatInputView detectThresholdView = new FloatInputView(
                "Detect Threshold",
                controller.getDetectThreshold(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setDetectThreshold(value.floatValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Detect Threshold\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        detectThresholdView.setToolTip("The minimum corner intensity required. Try 1.  " +
                "Feature intensity is computed using a Difference-of-Gaussian operation, " +
                "which can have negative values. Set to -Float.MAX_VALUE to detect every " +
                "possible feature");
        addInputView(detectThresholdView);

        // Extract Radius
        IntegerInputView extractRadiusView = new IntegerInputView(
                "Extract Radius",
                controller.getExtractRadius(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setExtractRadius(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Extract Radius\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        extractRadiusView.setToolTip("Adjust the radius of the feature to use. Try 2");
        addInputView(extractRadiusView);

        // Features per scale
        IntegerInputView featuresPerScaleView = new IntegerInputView(
                "Features per Scale",
                controller.getFeaturesPerScale(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setFeaturesPerScale(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Features per Scale\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        featuresPerScaleView.setToolTip("Adjust the max detected features per scale.  " +
                "A tuning parameter that is image dependent.  Disable with any value less then 0");
        addInputView(featuresPerScaleView);

        // Edge Threshold.
        DoubleInputView edgeThreshold = new DoubleInputView(
                "Edge Rejection Threshold",
                controller.getEdgeThreshold(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setEdgeThreshold(value.doubleValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Edge Rejection Threshold\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        edgeThreshold.setToolTip("Edge rejection threshold.  Decrease to eliminate more keypoints.  " +
                "Try 5.  Disable with a value <= 0");
        addInputView(edgeThreshold);
    }
}

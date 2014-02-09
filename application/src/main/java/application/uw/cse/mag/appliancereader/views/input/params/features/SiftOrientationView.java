package application.uw.cse.mag.appliancereader.views.input.params.features;

import application.uw.cse.mag.appliancereader.views.input.DoubleInputView;
import application.uw.cse.mag.appliancereader.views.input.IntegerInputView;
import application.uw.cse.mag.appliancereader.views.input.NumberInputView;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.sift.OrientationController;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class SiftOrientationView extends ConfigView {

    private final OrientationController controller;

    public SiftOrientationView(OrientationController controller) {
        super("Sift Orientation", controller);
        if (controller == null)
            throw new NullPointerException("Orientation Controller is null");
        this.controller = controller;
        initializeControls();
    }

    @Override
    protected void initializeControls() {

        // Histogram Size.
        IntegerInputView histogramSizeView = new IntegerInputView(
                "Histogram Size",
                controller.getHistogramSize(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setHistogramSize(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Histogram Size\' " + msg);
                    }
                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        histogramSizeView.setToolTip("Configure the number of elements within the 3-D spatial histogram.  " +
                "Standard is 36.");
        addInputView(histogramSizeView);

        // Sigma to Radius.
        DoubleInputView sigmaToRadiusView = new DoubleInputView(
                "Sigma to Radius",
                controller.getSigmaToRadius(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setSigmaToRadius(value.doubleValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Sigma to Radius\' " + msg);
                    }
                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        sigmaToRadiusView.setToolTip("Adjust the sigma to region radius.  Try 2.5");
        addInputView(sigmaToRadiusView);

        // Sigma Enlarge.
        DoubleInputView sigmaEnlargeView = new DoubleInputView(
                "Sigma Enlarge",
                controller.getSigmaEnlarge(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setSigmaEnlarge(value.doubleValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Sigma Enlarge\' " + msg);
                    }
                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
    }
}

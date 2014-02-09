package application.uw.cse.mag.appliancereader.views.input.params.features;

import application.uw.cse.mag.appliancereader.views.input.DoubleInputView;
import application.uw.cse.mag.appliancereader.views.input.IntegerInputView;
import application.uw.cse.mag.appliancereader.views.input.NumberInputView;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.sift.DescribeController;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class SiftDescriberView extends ConfigView {


    /**
     *
     */
    private final DescribeController controller;

    /**
     *
     * @param controller
     */
    public SiftDescriberView(DescribeController controller) {
        super("Sift Describe", controller);
        if (controller == null)
            throw new NullPointerException(getClass().getSimpleName() + "(), Can't have null controller.");
        this.controller = controller;
        initializeControls();
    }

    @Override
    protected void initializeControls() {

        // Grid Width
        IntegerInputView gridWidthView = new IntegerInputView(
                "Grid Width", controller.getGridWidth(), new NumberInputView.InputListener() {
            @Override
            public void onValueChanged(Number value) {
                try {
                    controller.setGridWidth(value.intValue());
                } catch (ConfigException e) {
                    onErrorOccured(e.getMessage());
                }
            }
            @Override
            public void onErrorOccured(String msg) {
                showError("\'Grid Width\' " + msg);
            }

            @Override
            public void hideAllErrors() {
                hideError();
            }
        });
        gridWidthView.setToolTip("Number of grid elements along a side. Typically 4");
        addInputView(gridWidthView);

        // Number of Sample.
        IntegerInputView numSamplesView = new IntegerInputView(
                "Number of Samples",
                controller.getNumSamples(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setNumSamples(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Number of Samples\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        numSamplesView.setToolTip("Number of samples along a grid. Typically 8");
        addInputView(numSamplesView);

        // Histogram Bin
        IntegerInputView histBinsView = new IntegerInputView(
                "Histogram Bins",
                controller.getHistBins(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setNumHistBins(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Histogram Bins\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                }
        );
        histBinsView.setToolTip("Number of bins in the orientation histogram. Typically 8");
        addInputView(histBinsView);

        // Weight Sigma
        DoubleInputView weightSigmaView = new DoubleInputView(
                "Weight Sigma",
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
                        showError("\'Weight Sigma\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        weightSigmaView.setToolTip("Descriptor element's weighting from center. Typically 0.5");
        addInputView(weightSigmaView);

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
        sigmaToRadiusView.setToolTip("Conversation from scale space to pixels. Typically 3");
        addInputView(sigmaToRadiusView);
    }
}

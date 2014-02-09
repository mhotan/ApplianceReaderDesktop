package application.uw.cse.mag.appliancereader.views.input.params.features;

import application.uw.cse.mag.appliancereader.views.input.FloatInputView;
import application.uw.cse.mag.appliancereader.views.input.IntegerInputView;
import application.uw.cse.mag.appliancereader.views.input.NumberInputView;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.surf.FastHessianController;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class FastHessianView extends ConfigView {

    private static final String DETECT_THRESHOLD = "Detect Threshold";
    private static final String EXTRACT_RADIUS = "Extract Radius";
    private static final String SAMPLE_SIZE = "Initial Sample Size";
    private static final String SIZE = "Initial Size";
    private static final String MAX_FEATURES = "Max Features per Scale";
    private static final String NUM_OCTAVES = "Number of Octaves";
    private static final String SCALES_PER_OCTAVES = "Number of Scales per Octave";

    private final FastHessianController controller;

    public FastHessianView(FastHessianController controller) {
        super("Fast Hessian", controller);
        if (controller == null) throw new NullPointerException();
        this.controller = controller;
        initializeControls();
    }

    @Override
    protected void initializeControls() {

        // Detect Threshold
        FloatInputView detectThresholdView = new FloatInputView(
                DETECT_THRESHOLD,
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
                        showError("\'" + DETECT_THRESHOLD + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        detectThresholdView.setToolTip("Minimum feature intensity. Image dependent.  Start tuning at 1.");
        addInputView(detectThresholdView);

        // Extract Radius
        IntegerInputView extractRadius = new IntegerInputView(
                EXTRACT_RADIUS,
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
                        showError("\'" + EXTRACT_RADIUS + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                }
        );
        extractRadius.setToolTip("Radius used for non-max-suppression.  Typically 1 or 2.");
        addInputView(extractRadius);

        // Initial Sample Size
        IntegerInputView initSampSizeView = new IntegerInputView(
                SAMPLE_SIZE,
                controller.getInitialSampleSize(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setInitialSampleSize(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'" + SAMPLE_SIZE + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                }
        );
        initSampSizeView.setToolTip("How often pixels are sampled in the first octave.  " +
                "Typically 1 or 2.");
        addInputView(initSampSizeView);

        // Initial Size
        IntegerInputView initSizeView = new IntegerInputView(
                SIZE,
                controller.getInitialSize(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setInitialSize(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'" + SIZE + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                }
        );
        initSizeView.setToolTip("Size of Fast");
        addInputView(initSizeView);

        // Max Features per Scale
        IntegerInputView maxFeatPerScaleView = new IntegerInputView(
                MAX_FEATURES,
                controller.getMaxFeaturePerScale(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setMaxFeaturePerScale(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'" + MAX_FEATURES + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                }
        );
        maxFeatPerScaleView.setToolTip("Adjust Number of features it will find.  " +
                "\nTypically 1 or 2.  \nSet to a value less then or equal to 0 to return all features.");
        addInputView(maxFeatPerScaleView);

        // Number of Octaves
        IntegerInputView numOctaves = new IntegerInputView(
                NUM_OCTAVES,
                controller.getNumberOfOctaves(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setNumberOfOctaves(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'" + NUM_OCTAVES + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                }
        );
        numOctaves.setToolTip("Typically 4");
        addInputView(numOctaves);

        // Number of Scales per Octave
        IntegerInputView numScalePerOctave = new IntegerInputView(
                SCALES_PER_OCTAVES,
                controller.getNumberScalesPerOctave(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setNumberScalesPerOctave(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'" + SCALES_PER_OCTAVES + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                }
        );
        numScalePerOctave.setToolTip("Typically 4.");
        addInputView(numScalePerOctave);

    }


}

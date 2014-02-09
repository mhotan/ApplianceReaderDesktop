package application.uw.cse.mag.appliancereader.views.input.params.features;

import application.uw.cse.mag.appliancereader.views.input.BooleanInputView;
import application.uw.cse.mag.appliancereader.views.input.FloatInputView;
import application.uw.cse.mag.appliancereader.views.input.IntegerInputView;
import application.uw.cse.mag.appliancereader.views.input.NumberInputView;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.sift.ScaleSpaceController;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class SiftScaleSpaceView extends ConfigView {

    private final ScaleSpaceController controller;

    public SiftScaleSpaceView(ScaleSpaceController controller) {
        super("Sift Scale Space", controller);
        if (controller == null) throw new NullPointerException();
        this.controller = controller;
        initializeControls();
    }

    @Override
    protected void initializeControls() {

        // Blur Sigma
        FloatInputView blurSigmaView = new FloatInputView(
                "Blur Sigma",
                controller.getBlurSigma(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setBlurSigma(value.floatValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Blur Sigma\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        blurSigmaView.setToolTip("Amount to blur to apply to each scale inside an octave.");
        addInputView(blurSigmaView);

        // Double Input Image
        BooleanInputView doubleInputImageView = new BooleanInputView(
                "Double Input Image",
                controller.isDoubleInputImage(),
                new BooleanInputView.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(boolean value) {
                        try {
                            controller.setDoubleInputImage(value);
                        } catch (ConfigException e) {
                            showError("\'Double Input Image\' " + e.getMessage());
                        }
                    }
                });
        doubleInputImageView.setToolTip("Double the image.");
        addInputView(doubleInputImageView);

        // Number of Octaves
        IntegerInputView numOctavesView = new IntegerInputView(
                "Number of Octaves",
                controller.getNumOctaves(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setNumOctaves(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Number of Octaves\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        numOctavesView.setToolTip("Increasing the scale by an octave means doubling the size of the smoothing kernel," +
                "     whose effect is roughly equivalent to halving the image resolution.  By default the scale spans" +
                "     as many octaves as possible.  Which has the effect of searching key points of all possible sizes.");
        addInputView(numOctavesView);

        // Number of Scales
        IntegerInputView numScalesView = new IntegerInputView(
                "Number of Scales",
                controller.getNumScales(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setNumScales(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Number of Scales\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        numScalesView.setToolTip("Adjust the number of scales per octave.  " +
                "Increasing this number might in principle return more" +
                " refined keypoints, but in practice can make their selection unstable due to noise.");
        addInputView(numScalesView);

    }
}

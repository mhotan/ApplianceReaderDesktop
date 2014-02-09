package application.uw.cse.mag.appliancereader.views.input.params.features;

import application.uw.cse.mag.appliancereader.views.input.DoubleInputView;
import application.uw.cse.mag.appliancereader.views.input.NumberInputView;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.surf.SurfDescriptorSpeedController;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class SurfDescriptorSpeedView extends ConfigView {

    private static final String WEIGHT_SIGMA = "Weight Sigma";

    private final SurfDescriptorSpeedController controller;

    public SurfDescriptorSpeedView(SurfDescriptorSpeedController controller) {
        super("Surf Descriptor Speed", controller);
        if (controller == null)
            throw new NullPointerException();
        this.controller = controller;
        initializeControls();
    }

    @Override
    protected void initializeControls() {

        // Weight Sigma.
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
        weightSigmaView.setToolTip("Weighting factor's sigma.\n" +
                "Try 3.8");
        addInputView(weightSigmaView);

    }
}

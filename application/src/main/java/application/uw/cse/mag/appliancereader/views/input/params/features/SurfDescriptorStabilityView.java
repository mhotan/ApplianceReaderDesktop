package application.uw.cse.mag.appliancereader.views.input.params.features;

import application.uw.cse.mag.appliancereader.views.input.DoubleInputView;
import application.uw.cse.mag.appliancereader.views.input.IntegerInputView;
import application.uw.cse.mag.appliancereader.views.input.NumberInputView;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.surf.SurfDescriptorStabilityController;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class SurfDescriptorStabilityView extends ConfigView {

    private static final String OVERLAP = "Overlap";
    private static final String SIGMA_LARGE_GRID = "Sigma Large Grid";
    private static final String SIGMA_SUBREGION = "Sigma Sub Region";

    private final SurfDescriptorStabilityController controller;

    public SurfDescriptorStabilityView(SurfDescriptorStabilityController controller) {
        super("Surf Descriptor Stability", controller);
        if (controller == null)
            throw new NullPointerException();
        this.controller = controller;
        initializeControls();
    }

    @Override
    protected void initializeControls() {

        // Overlap
        IntegerInputView overlapView = new IntegerInputView(
                OVERLAP,
                controller.getOverLap(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setOverLap(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'" + OVERLAP + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                }
        );
        overlapView.setToolTip("Adjust the number of sample points sub-regions that overlap.\n" +
                "Typically 2");
        addInputView(overlapView);

        // Sigma Large Grid
        DoubleInputView sigmaLargeGridView = new DoubleInputView(
                SIGMA_LARGE_GRID,
                controller.getSigmaLargeGrid(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setSigmaLargeGrid(value.doubleValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'" + SIGMA_LARGE_GRID + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                }
        );
        sigmaLargeGridView.setToolTip("Adjust the sigma used to weight points in the sub-region grid. \n" +
                "Typically 2.5.");
        addInputView(sigmaLargeGridView);

        // Sigma Sub Region.
        DoubleInputView sigmaSubRegionView = new DoubleInputView(
                SIGMA_SUBREGION,
                controller.getSigmaSubRegion(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            controller.setSigmaSubRegion(value.doubleValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'" + SIGMA_SUBREGION + "\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                }
        );

    }
}

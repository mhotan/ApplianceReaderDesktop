package application.uw.cse.mag.appliancereader.views.input.params.features;

import uw.cse.mag.appliancereader.lib.cv.params.surf.AverageIntegralController;
import uw.cse.mag.appliancereader.lib.cv.params.surf.FastHessianController;
import uw.cse.mag.appliancereader.lib.cv.params.surf.FastSurfParams;
import uw.cse.mag.appliancereader.lib.cv.params.surf.SurfDescriptorSpeedController;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class FastSurfParametersView extends ParametersView {

    public FastSurfParametersView(FastSurfParams params) {
        super(params);

        FastHessianController fastHessianController = params.getFastHessianController();
        SurfDescriptorSpeedController speedController = params.getSpeedController();
        AverageIntegralController avgIntController = params.getAvgIntegralController();

        addConfigView(new FastHessianView(fastHessianController));
        addConfigView(new SurfDescriptorSpeedView(speedController));
        addConfigView(new AverageIntegralView(avgIntController));
    }

    @Override
    public String getTitle() {
        return "Fast Surf";
    }
}

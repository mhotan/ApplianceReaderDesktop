package application.uw.cse.mag.appliancereader.views.input.params.features;

import uw.cse.mag.appliancereader.lib.cv.params.surf.FastHessianController;
import uw.cse.mag.appliancereader.lib.cv.params.surf.SlidingIntegralController;
import uw.cse.mag.appliancereader.lib.cv.params.surf.StableSurfParams;
import uw.cse.mag.appliancereader.lib.cv.params.surf.SurfDescriptorStabilityController;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class StableSurfParametersView extends ParametersView {

    public StableSurfParametersView(StableSurfParams params) {
        super(params);

        FastHessianController fastHessianController = params.getFastHessianController();
        SurfDescriptorStabilityController stabilityController = params.getStabilityController();
        SlidingIntegralController slidingController = params.getSlidingIntegralController();

        addConfigView(new FastHessianView(fastHessianController));
        addConfigView(new SurfDescriptorStabilityView(stabilityController));
        addConfigView(new SlidingIntegralView(slidingController));
    }

    @Override
    public String getTitle() {
        return "Stable Surf";
    }
}

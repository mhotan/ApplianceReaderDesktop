package application.uw.cse.mag.appliancereader.views.input.params.features;

import uw.cse.mag.appliancereader.lib.cv.params.sift.*;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class SiftParametersView extends ParametersView {

    public SiftParametersView(SiftParams params) {
        super(params);

        // Obtain all the controls.
        ScaleSpaceController scaleSpaceController = params.getScaleSpaceController();
        DetectorController detectorController = params.getDetectorController();
        OrientationController orientationController = params.getOrientationController();
        DescribeController describerController = params.getDescribeController();

        // Add a configuration view.
        addConfigView(new SiftScaleSpaceView(scaleSpaceController));
        addConfigView(new SiftDetectorView(detectorController));
        addConfigView(new SiftOrientationView(orientationController));
        addConfigView(new SiftDescriberView(describerController));
    }

    @Override
    public String getTitle() {
        return "Sift";
    }
}

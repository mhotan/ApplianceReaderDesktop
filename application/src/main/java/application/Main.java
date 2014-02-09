package application;

import application.uw.cse.mag.appliancereader.MainView;
import application.uw.cse.mag.appliancereader.views.CompleteParametersView;
import application.uw.cse.mag.appliancereader.views.FeatureDetectorsView;
import application.uw.cse.mag.appliancereader.views.ResultView;
import application.uw.cse.mag.appliancereader.views.input.params.features.FastSurfParametersView;
import application.uw.cse.mag.appliancereader.views.input.params.features.ParametersView;
import application.uw.cse.mag.appliancereader.views.input.params.features.SiftParametersView;
import application.uw.cse.mag.appliancereader.views.input.params.features.StableSurfParametersView;
import application.uw.cse.mag.appliancereader.views.input.params.homography.HomographyParametersView;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.ejml.data.DenseMatrix64F;
import uw.cse.mag.appliancereader.lib.cv.HomographyCalculator;
import uw.cse.mag.appliancereader.lib.cv.ImagePointAssociator;
import uw.cse.mag.appliancereader.lib.cv.params.core.FeatureDetectionParams;
import uw.cse.mag.appliancereader.lib.cv.params.homography.HomographyParams;
import uw.cse.mag.appliancereader.lib.cv.params.sift.SiftParams;
import uw.cse.mag.appliancereader.lib.cv.params.surf.FastSurfParams;
import uw.cse.mag.appliancereader.lib.cv.params.surf.StableSurfParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mhotan_dev on 12/18/13.
 */
public class Main extends Application implements
        FeatureDetectorsView.OnFeatureDetectorChangedListener,
        MainView.MainViewListener,
        HomographyCalculator.OnHomographyChangedListener {

    private HomographyCalculator calculator;

    private Map<FeatureDetectionParams, ImagePointAssociator> associatorMap;

    public static final double IMAGE_WIDTH = 600;
    public static final double IMAGE_HEIGHT = 400;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Appliance Reader Algorithm Tuner");

        // Set the size of the primary stage.
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // Set the window to be the size of the screen.
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        // Create the feature detection parameters
        SiftParams siftParams = new SiftParams();
        FastSurfParams fastSurfParams = new FastSurfParams();
        StableSurfParams stableSurfParams = new StableSurfParams();

        // Create views for each parameter
        SiftParametersView siftParamView = new SiftParametersView(siftParams);
        FastSurfParametersView fastSurfParamView = new FastSurfParametersView(fastSurfParams);
        StableSurfParametersView stableSurfParamView = new StableSurfParametersView(stableSurfParams);

        // Create the homogrpahy parameter and its view.
        HomographyParams homoParams = new HomographyParams();
        HomographyParametersView homoParamView = new HomographyParametersView(homoParams);

        // Cache that hold quick references between parameters and ImagePointAssociator.
        associatorMap = new HashMap<FeatureDetectionParams, ImagePointAssociator>();

        // Assign the calculator.
        calculator = new HomographyCalculator(homoParams);

        // Create a list of views.
        List<ParametersView> viewList = new ArrayList<ParametersView>(3);
        viewList.add(siftParamView);
        viewList.add(fastSurfParamView);
        viewList.add(stableSurfParamView);

        // Create teh Feature detectors view and register listener.
        FeatureDetectorsView allDetectorViews = new FeatureDetectorsView(viewList, this);
        calculator.addListener(this);

        // Create a view to switch between different detectors.
        CompleteParametersView completeParamView = new CompleteParametersView(
                homoParamView, allDetectorViews);

        // Result View
        ResultView resultView = new ResultView(calculator);

        // Build the Main view.
        MainView root = new MainView(this);
        root.setParameters(completeParamView);
        root.setResultView(resultView);


        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void onFeatureParameterChanged(FeatureDetectionParams newVal, FeatureDetectionParams oldVal) {
        ImagePointAssociator associator = associatorMap.get(newVal);
        if (associator == null) {
            associator = new ImagePointAssociator(newVal);
            associatorMap.put(newVal, associator);
        }
        calculator.setImageAssociator(associator);
    }

    @Override
    public void onHomographyChanged(DenseMatrix64F newHomography, DenseMatrix64F oldHomography) {

    }

    @Override
    public void onImagesUpdated(Image image1, Image image2) {
        if (image1 != null)
            calculator.setReferenceImage(SwingFXUtils.fromFXImage(image1, null));
        if (image2 != null)
            calculator.setOtherImage(SwingFXUtils.fromFXImage(image2, null));
    }

    @Override
    public void onError(String error) {

    }
}

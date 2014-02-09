package application.uw.cse.mag.appliancereader.views;

import application.uw.cse.mag.appliancereader.util.FXMLPaneLoader;
import application.uw.cse.mag.appliancereader.views.input.params.homography.HomographyParametersView;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by mhotan_dev on 12/30/13.
 */
public class CompleteParametersView extends BorderPane {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Tab featureTab;

    @FXML
    private Tab homographyTab;

    private final HomographyParametersView mHomographyView;
    private final FeatureDetectorsView mFeatureViews;

    public CompleteParametersView(HomographyParametersView homoView,
                                  FeatureDetectorsView featureViews) {
        FXMLPaneLoader.load(this, "CompleteParametersView.fxml");
        if (homoView == null)
            throw new NullPointerException();
        if (featureViews == null)
            throw new NullPointerException();
        this.mHomographyView = homoView;
        this.mFeatureViews = featureViews;

        featureTab.setContent(mFeatureViews);
        homographyTab.setContent(mHomographyView);
    }

    @FXML
    void initialize() {
        assert featureTab != null : "fx:id=\"featureTab\" was not injected: check your FXML file 'CompleteParametersView.fxml'.";
        assert homographyTab != null : "fx:id=\"homographyTab\" was not injected: check your FXML file 'CompleteParametersView.fxml'.";
    }



}

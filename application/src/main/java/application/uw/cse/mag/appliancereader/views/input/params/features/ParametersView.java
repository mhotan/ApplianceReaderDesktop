package application.uw.cse.mag.appliancereader.views.input.params.features;

import application.uw.cse.mag.appliancereader.util.FXMLPaneLoader;
import application.uw.cse.mag.appliancereader.views.input.BooleanInputView;
import application.uw.cse.mag.appliancereader.views.input.DoubleInputView;
import application.uw.cse.mag.appliancereader.views.input.IntegerInputView;
import application.uw.cse.mag.appliancereader.views.input.NumberInputView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import uw.cse.mag.appliancereader.lib.cv.params.core.FeatureDetectionParams;
import uw.cse.mag.appliancereader.lib.cv.params.core.Scorer;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public abstract class ParametersView extends BorderPane {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    protected VBox controllerPane, persistentControls;

    @FXML
    protected Label label, error;

    @FXML
    protected ChoiceBox<Scorer> scorerChoiceBox;

    /**
     * Parameters to draw.
     */
    private final FeatureDetectionParams params;

    /**
     * Creates a parameters view.
     *
     * @param params Parameters to show.
     */
    public ParametersView(FeatureDetectionParams params) {
        FXMLPaneLoader.load(this, "ParameterView.fxml");

        if (params == null)
            throw new NullPointerException(getClass().getSimpleName() + "(), Null parameters.");
        this.params = params;

        // Make sure error dissapears
        error.managedProperty().bind(error.visibleProperty());
        error.setVisible(false);

        // Set the title.
        label.setText(getTitle());

        initializeControls();
    }

    protected void initializeControls() {
        // Set the value of the scorer choice box.
        scorerChoiceBox.setItems(FXCollections.observableArrayList(Scorer.values()));
        // Set to the initial value and then listen for any changes.
        scorerChoiceBox.getSelectionModel().select(this.params.getScorerEnum());
        scorerChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Scorer>() {
            @Override
            public void changed(ObservableValue<? extends Scorer> ov, Scorer value, Scorer new_value) {
                params.setScorer(new_value);
            }
        });

        // Square Euclidian View.
        BooleanInputView squareEuclideanView = new BooleanInputView(
                "Square Euclidean",
                this.params.isSquareEuclidian(),
                new BooleanInputView.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(boolean value) {
                        params.setSquareEuclidian(value);
                    }
                });
        squareEuclideanView.setToolTip("Declare whether the Euclidean distance will be squared " +
                "for Score Association.");
        persistentControls.getChildren().add(squareEuclideanView);

        // Backwards Validation parameter.
        BooleanInputView backwardsValidationView = new BooleanInputView(
                "Backwards Validation",
                this.params.isBackwardsValidated(),
                new BooleanInputView.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(boolean value) {
                        params.setBackwardsValidation(value);
                    }
                });
        backwardsValidationView.setToolTip("Declare whether Score Association requires forward and backwards " +
                "association.");
        persistentControls.getChildren().add(backwardsValidationView);

        // Maximum distance between features
        DoubleInputView maxDistanceView = new DoubleInputView(
                "Maximum Feature Distance",
                this.params.getMaxDistance(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        params.setMaxDistance(value.doubleValue());
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError(msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        maxDistanceView.canBeNegative(false);
        maxDistanceView.canBeZero(false);
        persistentControls.getChildren().add(maxDistanceView);

        // Maximum features
        IntegerInputView maxFeaturesView = new IntegerInputView(
                "Maximum Associated Features",
                this.params.getMaxFeatures(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        params.setMaxFeatures(value.intValue());
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError(msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                }
        );
        maxFeaturesView.canBeNegative(false);
        maxFeaturesView.canBeZero(false);
        persistentControls.getChildren().add(maxFeaturesView);

    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    ////    Setters
    //////////////////////////////////////////////////////////////////////////////////////////////

    protected void addConfigView(ConfigView view) {
        if (view == null) return;
        if (controllerPane.getChildren().contains(view)) return;
        controllerPane.getChildren().add(view);
    }

    protected void hideError() {
        error.setVisible(false);
    }

    protected void showError(String error) {
        if (error == null) {
            hideError();
            return;
        }
        this.error.setText("Error: " + error);
        this.error.setVisible(true);
    }

    /**
     * @return The title of this parameter view.
     */
    public abstract String getTitle();

    //////////////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    //////////////////////////////////////////////////////////////////////////////////////////////


    public FeatureDetectionParams getParameters() {
        return params;
    }

    @FXML
    void initialize() {
        assert controllerPane != null : "fx:id=\"controllerPane\" was not injected: check your FXML file 'ParameterView.fxml'.";
        assert error != null : "fx:id=\"error\" was not injected: check your FXML file 'ParameterView.fxml'.";
        assert label != null : "fx:id=\"label\" was not injected: check your FXML file 'ParameterView.fxml'.";
        assert persistentControls != null : "fx:id=\"persistentControls\" was not injected: check your FXML file 'ParameterView.fxml'.";
        assert scorerChoiceBox != null : "fx:id=\"scorerChoiceBox\" was not injected: check your FXML file 'ParameterView.fxml'.";

    }

}

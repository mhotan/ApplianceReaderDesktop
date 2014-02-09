package application.uw.cse.mag.appliancereader.views.input.params.homography;

import application.uw.cse.mag.appliancereader.util.FXMLPaneLoader;
import application.uw.cse.mag.appliancereader.views.input.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import uw.cse.mag.appliancereader.lib.cv.params.core.ConfigException;
import uw.cse.mag.appliancereader.lib.cv.params.homography.ErrorMetric;
import uw.cse.mag.appliancereader.lib.cv.params.homography.HomographyParams;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by mhotan_dev on 12/30/13.
 */
public class HomographyParametersView extends BorderPane {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox controllerPane;

    @FXML
    protected Label error, label;

    @FXML
    private ChoiceBox<ErrorMetric> errorMetricChoiceBox;

    private final HomographyParams parameters;

    public HomographyParametersView(HomographyParams params) {
        FXMLPaneLoader.load(this, "ParametersView.fxml");

        if (params == null) throw new NullPointerException();
        parameters = params;

        // Make sure error dissapears
        error.managedProperty().bind(error.visibleProperty());
        error.setVisible(false);

        // Set the title.
        label.setText("Homography Parameters");
        initializeControls();
    }

    protected void initializeControls() {
        // Set the value of the Error Metric choice box.
        ObservableList<ErrorMetric> list = FXCollections.observableArrayList(ErrorMetric.values());
        errorMetricChoiceBox.setItems(list);
        // Set to the initial value and then listen for any changes.
        errorMetricChoiceBox.getSelectionModel().select(this.parameters.getErrorMetric());
        errorMetricChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ErrorMetric>() {
            @Override
            public void changed(ObservableValue<? extends ErrorMetric> ov, ErrorMetric old_value, ErrorMetric new_value) {
                parameters.setErrorMetric(new_value);
            }
        });

        // Is Normalize
        BooleanInputView isNormalizedView = new BooleanInputView(
                "Normalize?",
                parameters.isNormalize(),
                new BooleanInputView.OnValueChangedListener() {
                    @Override
                    public void onValueChanged(boolean value) {
                        parameters.setNormalize(value);
                    }
                });
        isNormalizedView.setToolTip("Should we normalize the image input.");
        addInputView(isNormalizedView);

        // Number Iterations
        IntegerInputView numIterationsView = new IntegerInputView(
                "Number of Iterations",
                parameters.getNumIterations(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            parameters.setNumIterations(value.intValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Number of Iterations\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                }
        );
        numIterationsView.setToolTip("The maximum number of iterations " +
                "the RANSAC algorithm will perform.");
        addInputView(numIterationsView);

        // Threshold Fit
        DoubleInputView thresholdFitView = new DoubleInputView(
                "Fit Threshold",
                parameters.getThresholdFit(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            parameters.setThresholdFit(value.doubleValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Fit Threshold\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        thresholdFitView.setToolTip("How close of a fit a points needs to be " +
                "to the model to be considered a fit. In pixels");
        addInputView(thresholdFitView);

        // Randomize Seed
        LongInputView randomizedSeedView = new LongInputView(
                "Randomize Seed",
                parameters.getRandomizeSeed(),
                new NumberInputView.InputListener() {
                    @Override
                    public void onValueChanged(Number value) {
                        try {
                            parameters.setThresholdFit(value.longValue());
                        } catch (ConfigException e) {
                            onErrorOccured(e.getMessage());
                        }
                    }

                    @Override
                    public void onErrorOccured(String msg) {
                        showError("\'Randomize Seed\' " + msg);
                    }

                    @Override
                    public void hideAllErrors() {
                        hideError();
                    }
                });
        randomizedSeedView.setToolTip("Provides the randomization seed.  " +
                "This seed will be used by the random number generation.");
        addInputView(randomizedSeedView);
    }

    private void addInputView(InputView view) {
        controllerPane.getChildren().add(view);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    ////    Setters
    //////////////////////////////////////////////////////////////////////////////////////////////

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

    @FXML
    void initialize() {
        assert controllerPane != null : "fx:id=\"controllerPane\" was not injected: check your FXML file 'ParametersView.fxml'.";
        assert error != null : "fx:id=\"error\" was not injected: check your FXML file 'ParametersView.fxml'.";
        assert label != null : "fx:id=\"label\" was not injected: check your FXML file 'ParametersView.fxml'.";
        assert errorMetricChoiceBox != null : "fx:id=\"scorerChoiceBox\" was not injected: check your FXML file 'ParametersView.fxml'.";


    }

}

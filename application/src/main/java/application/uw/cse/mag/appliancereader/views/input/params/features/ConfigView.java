package application.uw.cse.mag.appliancereader.views.input.params.features;

import application.uw.cse.mag.appliancereader.util.FXMLPaneLoader;
import application.uw.cse.mag.appliancereader.views.input.InputView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import uw.cse.mag.appliancereader.lib.cv.params.core.BaseController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public abstract class ConfigView extends BorderPane {

    @FXML
    protected ResourceBundle resources;

    @FXML
    protected URL location;

    @FXML
    protected Label error, label;

    @FXML
    protected VBox inputPane;

    @FXML
    protected CheckBox useDefaultCheckBox;

    /**
     * Name to present this configuration with.
     */
    protected final String name;

    /**
     * The BaseController.
     */
    protected final BaseController controller;

    public ConfigView(String name, BaseController controller) {
        if (name == null)
            throw new IllegalArgumentException(getClass().getSimpleName() + "(), Name cannot be null");
        if (controller == null)
            throw new IllegalArgumentException(getClass().getSimpleName() + "(), Base Controller cannot be null");

        // Load the view.
        FXMLPaneLoader.load(this, "ConfigView.fxml");

        // Set the error label.
        error.managedProperty().bind(error.visibleProperty());
        error.setVisible(false);

        // Set the Base Controller.
        this.controller = controller;

        // Set the name of the View.
        this.name = name;
        label.setText(this.name);

        // Bind the layout property to the visbility property.
        inputPane.managedProperty().bind(inputPane.visibleProperty());
        inputPane.visibleProperty().bind(useDefaultCheckBox.selectedProperty().not());

        // Handle the checkbox button
        final BaseController cont = this.controller;
        useDefaultCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean selected = useDefaultCheckBox.selectedProperty().getValue();
                cont.setDefaulted(selected);
            }
        });

        // Set the selected property of the check box.
        useDefaultCheckBox.selectedProperty().setValue(this.controller.isDefaulted());

        // make sure this never grows.
        VBox.setVgrow(this, Priority.NEVER);
    }

    @FXML
    void initialize() {
        assert error != null : "fx:id=\"error\" was not injected: check your FXML file 'ConfigView.fxml'.";
        assert inputPane != null : "fx:id=\"inputPane\" was not injected: check your FXML file 'ConfigView.fxml'.";
        assert label != null : "fx:id=\"label\" was not injected: check your FXML file 'ConfigView.fxml'.";
        assert useDefaultCheckBox != null : "fx:id=\"useDefaultCheckBox\" was not injected: check your FXML file 'ConfigView.fxml'.";
    }

    protected void addInputView(InputView view) {
        inputPane.getChildren().add(view);
    }

    public void hideError() {
        error.setVisible(false);
    }

    public void showError(String error) {
        if (error == null) {
            hideError();
            return;
        }
        this.error.setText("Error: " + error);
        this.error.setVisible(true);
    }

    protected abstract void initializeControls();

}

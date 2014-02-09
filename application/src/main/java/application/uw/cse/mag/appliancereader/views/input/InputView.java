package application.uw.cse.mag.appliancereader.views.input;

import application.uw.cse.mag.appliancereader.util.FXMLPaneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Abstract class that represents an input.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public abstract class InputView extends HBox {

    @FXML
    protected ResourceBundle resources;

    @FXML
    protected URL location;

    @FXML
    protected Label inputLabel;

    protected final String label;

    public InputView(String label, String resourceFileName) {
        FXMLPaneLoader.load(this, resourceFileName);
        setLabel(label);
        this.label = label;
    }

    /**
     * Set the text for the label.
     *
     * @param label Label for the input type
     */
    private void setLabel(String label) {
        if (label == null || label.trim().isEmpty())
            throw new IllegalArgumentException("Illegal Label: " + label);

        label = label.trim();
        if (!label.endsWith(":"))
            label += ":";
        inputLabel.setText(label);
    }

    @FXML
    void initialize() {
        assert inputLabel != null : "fx:id=\"inputField\" was not injected: check your FXML file 'TextInput.fxml'.";
    }

    /**
     *
     *
     * @param tooltip String representation of tooltip.
     */
    public void setToolTip(String tooltip) {
        if (tooltip == null) return;
        inputLabel.setTooltip(new Tooltip(tooltip));
    }
}

package application.uw.cse.mag.appliancereader.views.input;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 * Creates an input view for
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class BooleanInputView extends InputView {

    /**
     *
     */
    private static final String FILE_NAME = "BooleanInput.fxml";

    @FXML
    protected RadioButton trueButton, falseButton;

    /**
     * Group of Radio Buttons
     */
    protected final ToggleGroup group;

    /**
     * Listener for value change
     */
    private final OnValueChangedListener listener;

    /**
     *
     *
     * @param label
     * @param initValue
     * @param listener
     */
    public BooleanInputView(String label, boolean initValue, OnValueChangedListener listener) {
        super(label, FILE_NAME);
        if (listener == null) {
            throw new NullPointerException(getClass().getSimpleName() + "(), Null Listener.");
        }
        group = new ToggleGroup();
        this.listener = listener;

        initializeControls();
        setValue(initValue);
    }


    /**
     * Initialize the controls of the Toggle Group.
     */
    private void initializeControls() {
        trueButton.setToggleGroup(this.group);
        falseButton.setToggleGroup(this.group);
        this.group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (new_toggle.equals(trueButton)) {
                    listener.onValueChanged(true);
                } else if (new_toggle.equals(falseButton)) {
                    listener.onValueChanged(false);
                } else {
                    // Error
                }
            }
        });
    }

    /**
     *
     *
     * @param value The value to set this
     */
    void setValue(boolean value) {
        if (value) {
            trueButton.setSelected(true);
            return;
        }
        falseButton.setSelected(true);
    }

    /**
     *
     */
    public interface OnValueChangedListener {

        public void onValueChanged(boolean value);

    }

    @FXML
    void initialize() {
        super.initialize();
        assert falseButton != null : "fx:id=\"falseButton\" was not injected: check your FXML file 'BooleanInput.fxml'.";
        assert trueButton != null : "fx:id=\"trueButton\" was not injected: check your FXML file 'BooleanInput.fxml'.";
    }

}

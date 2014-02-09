package application.uw.cse.mag.appliancereader.views.input;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 *
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 * @param <T> Type of number for this view.
 */
public abstract class NumberInputView<T extends Number> extends InputView {

    private static final String FILE_NAME = "TextInput.fxml";

    @FXML
    protected TextField inputField;

    private final InputListener listener;

    private boolean negative, zero, positive;

    /**
     *
     *
     * @param label Label to set
     * @param defaultValue The value to initially set parameter for.
     * @param listener
     */
    public NumberInputView(String label, T defaultValue, InputListener listener) {
        super(label, FILE_NAME);
        if (defaultValue == null)
            throw new NullPointerException("Can't have null default value.");
        if (listener == null)
            throw new NullPointerException("Can't have null listener");
        this.listener = listener;
        negative = zero = positive = true;
        inputField.setText("" + defaultValue);
    }

    @FXML
    void initialize() {
        super.initialize();
        assert inputField != null : "fx:id=\"inputField\" was not injected: check your FXML file 'TextInput.fxml'.";
    }

    /**
     * This Function validates that the rawValue input can be correctly transformed into the
     * desired value.  If the value
     *
     * @param rawValue The raw value of this particular type
     * @return null if validated, The error message if not validated.
     */
    protected abstract String validate(String rawValue);

    /**
     *
     *
     * @return The value associated with this raw value.
     */
    protected abstract T getValue(String rawValue);

    @FXML
    void setValue(ActionEvent event) {
        String rawVal = inputField.textProperty().getValue();
        String error = validate(rawVal);
        if (error != null) {
            this.listener.onErrorOccured("\'" + label + "\' " + error);
            return;
        }
        Number value = getValue(rawVal);
        if (!negative && value.doubleValue() < 0) {
            this.listener.onErrorOccured("\'" + label + "\'Cannot be Negative");
            return;
        }
        if (!zero && Double.compare(value.doubleValue(), 0) == 0) {
            this.listener.onErrorOccured("\'" + label + "\'Cannot be Zero");
            return;
        }
        if (!positive && value.doubleValue() > 0) {
            this.listener.onErrorOccured("\'" + label + "\'Cannot be Positive");
            return;
        }

        // Show no error occured.
        this.listener.hideAllErrors();
        this.listener.onValueChanged(value);
    }

    public void canBeNegative(boolean negative) {
        this.negative = negative;
    }

    public void canBeZero(boolean zero) {
        this.zero = zero;
    }

    public void canBePositive(boolean positive) {
        this.positive = positive;
    }

    public interface InputListener {

        public void onValueChanged(Number value);

        public void onErrorOccured(String msg);

        public void hideAllErrors();
    }

}

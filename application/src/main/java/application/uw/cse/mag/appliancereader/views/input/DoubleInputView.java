package application.uw.cse.mag.appliancereader.views.input;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class DoubleInputView extends NumberInputView<Double> {

    public DoubleInputView(String label, Double defaultValue, InputListener listener) {
        super(label, defaultValue, listener);
    }

    @Override
    protected String validate(String rawValue) {
        Double val = null;
        try {
            val = Double.valueOf(rawValue);
        } catch (NumberFormatException e) {
            return "\'" + this.label + "\' " + e.getMessage();
        }
        return null;
    }

    @Override
    protected Double getValue(String rawValue) {
        return Double.valueOf(rawValue);
    }
}

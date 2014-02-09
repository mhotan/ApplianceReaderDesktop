package application.uw.cse.mag.appliancereader.views.input;

import application.uw.cse.mag.appliancereader.views.input.NumberInputView;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class FloatInputView extends NumberInputView<Float> {

    public FloatInputView(String label, Float defaultValue, InputListener listener) {
        super(label, defaultValue, listener);
    }

    @Override
    protected String validate(String rawValue) {
        Float val = null;
        try {
            val = Float.valueOf(rawValue);
        } catch (NumberFormatException e) {
            return "\'" + this.label + "\' " + e.getMessage();
        }
        return null;
    }

    @Override
    protected Float getValue(String rawValue) {
        return Float.valueOf(rawValue);
    }
}

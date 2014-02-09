package application.uw.cse.mag.appliancereader.views.input;

/**
 * Created by mhotan_dev on 12/29/13.
 */
public class IntegerInputView extends NumberInputView<Integer> {

    public IntegerInputView(String label, Integer defaultValue, InputListener listener) {
        super(label, defaultValue, listener);
    }

    @Override
    protected String validate(String rawValue) {
        Integer val = null;
        try {
            val = Integer.valueOf(rawValue);
        } catch (NumberFormatException e) {
            return "\'" + this.label + "\' " + e.getMessage();
        }
        return null;
    }

    @Override
    protected Integer getValue(String rawValue) {
        return Integer.valueOf(rawValue);
    }
}

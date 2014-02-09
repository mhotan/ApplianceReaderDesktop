package application.uw.cse.mag.appliancereader.views.input;

/**
 * Created by mhotan_dev on 12/30/13.
 */
public class LongInputView extends NumberInputView<Long> {

    public LongInputView(String label, Long defaultValue, InputListener listener) {
        super(label, defaultValue, listener);
    }

    @Override
    protected String validate(String rawValue) {
        Long val = null;
        try {
            val = Long.valueOf(rawValue);
        } catch (NumberFormatException e) {
            return "\'" + this.label + "\' " + e.getMessage();
        }
        return null;
    }

    @Override
    protected Long getValue(String rawValue) {
        return Long.valueOf(rawValue);
    }
}

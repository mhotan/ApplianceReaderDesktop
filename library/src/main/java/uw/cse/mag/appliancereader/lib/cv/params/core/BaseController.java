package uw.cse.mag.appliancereader.lib.cv.params.core;

import boofcv.struct.Configuration;

import java.util.logging.Logger;

/**
 * Base Controller for augmenting Feature Detection paramater Configuration
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 * Created by mhotan_dev on 12/25/13.
 */
public abstract class BaseController {

    /**
     * Check if the Orientation is defaulted.
     */
    private boolean mDefaulted;

    /**
     * Current Configuration
     */
    private Configuration mConfiguration;

    /**
     * Callback for listener to notify.
     */
    private OnConfigurationChangedListener mListener;

    protected BaseController() {
        this(null);
    }

    /**
     * Constructor that preassigns listener.
     * @param listener Listener class.
     */
    protected BaseController(OnConfigurationChangedListener listener) {
        mDefaulted = true;
        mListener = listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Mutator and Setters
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * Updates the state of this.  Potentially created new Configurations.
     *
     * @throws ConfigException There is a misrepresentation with validity of Configuration.
     */
    protected void update() throws ConfigException {
        Configuration newConfig = getCurrentConfiguration();
        try {
            newConfig.checkValidity();
        } catch (Exception e) {
            throw new ConfigException(e.getMessage());
        }

        Configuration oldVal = mConfiguration;
        mConfiguration = newConfig;
        // Notify Listeners.
        if (mListener != null)
            mListener.onConfigurationChanged(mDefaulted ? null : mConfiguration,
                    oldVal, getConfigurationType());
    }

    /**
     * Set the value of this Scale Space to defaulted.
     *
     * @param defaulted Flag whether the default value is desired.
     */
    public void setDefaulted(boolean defaulted) {
        if (mDefaulted == defaulted) return;
        mDefaulted = defaulted;
        try {
            update();
        } catch (ConfigException e) {
            Logger.getLogger(getClass().getSimpleName()).warning("Unable to default" +
                    " parameter.");
        }
    }

    /**
     * Set the change listener.
     *
     * @param listener Listening object or null to clear out any other listeners.
     */
    public void setListener(OnConfigurationChangedListener listener) {
        this.mListener = listener;
    }


    ////////////////////////////////////////////////////////////////////////////////////
    ////    Getters
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the current configuration of this element based of the state of this.
     *
     * @return Null if defaulted, Otherwise current Configuration state.
     */
    protected abstract Configuration getCurrentConfiguration();

    /**
     * @return Particular type of configuration.
     */
    public abstract ConfigurationType getConfigurationType();

    /**
     * @return Whether this Configuration is defaulted.
     */
    public Configuration getConfiguration() { return mConfiguration; }

    /**
     * @return Whether this Controller is defaulted.
     */
    public boolean isDefaulted() {
        return mDefaulted;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Supporting Interface
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseController)) return false;
        BaseController that = (BaseController) o;
        if (mDefaulted != that.mDefaulted) return false;
        if (!mConfiguration.equals(that.mConfiguration)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (mDefaulted ? 1 : 0);
        result = 31 * result + mConfiguration.hashCode();
        return result;
    }
}

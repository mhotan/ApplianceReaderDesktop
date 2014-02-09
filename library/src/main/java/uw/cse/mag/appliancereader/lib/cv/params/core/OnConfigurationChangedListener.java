package uw.cse.mag.appliancereader.lib.cv.params.core;

import boofcv.struct.Configuration;

/**
 * interface to notify any time Configuration changed.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public interface OnConfigurationChangedListener {

    /**
     * Callback to notify that Configuration has changed.
     *
     * @param newVal New value of Configuration.
     * @param oldVal Old value of Configuration.
     */
    public void onConfigurationChanged(Configuration newVal,
                                       Configuration oldVal,
                                       ConfigurationType type);

}

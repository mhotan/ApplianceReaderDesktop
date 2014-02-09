package uw.cse.mag.appliancereader.lib.cv.params.core;

/**
 * Exception for a configuration error.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class ConfigException extends Exception {

    /**
     *
     *
     * @param message Message of the error.
     */
   public ConfigException(String message) {
        super(message);
   }

}

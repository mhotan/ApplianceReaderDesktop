package application.uw.cse.mag.appliancereader.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

/**
 * A special class that helps wrap and handle the loading of custom
 * FXML layouts and views. It is up to clients to ensure that input panes
 * are of the same class type to the FXML document.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class FXMLPaneLoader {

    /**
     * Cannot Instantiate.
     */
    private FXMLPaneLoader() {}
    
    /**
     * Loads the FXML View by using the Simple name of the pane class.
     * <b>The resource to be loaded pane.getClass().getSimpleName() + ".fxml"</b>
     * <b>This special fxml resource file must be in the same directory structure as the pane
     * class but from the resource root directory.</b>
     *
     * @param pane Pane to load
     */
    public static void load(Pane pane) {
         load(pane, pane.getClass().getSimpleName() + ".fxml");
    }

    /**
     * Loads the resource for the input pane.  This is meant for constructors and
     * FXML file that are set for fx:root.
     * <b>The resource file must be in the same directory structure as the pane
     * class but from the resource root directory.</b>
     *
     * @param pane Pane to set as root and controller.
     * @param resourceName Name of the FXML file to load.
     */
    public static void load(Pane pane, String resourceName) {

        // Check if we are able to access the resource file.
        // The resource file name must be relative to the pane class package.
        URL url = pane.getClass().getResource(resourceName);
        if (url == null)
            throw new IllegalStateException(resourceName + " not found in " +
                    " resource package \"" + pane.getClass().getPackage() + "\"");

        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(pane);
        loader.setController(pane);

        try {
            loader.load();
        } catch (IOException e) {
             throw new RuntimeException(FXMLPaneLoader.class.getSimpleName() +
                    "() Unable to load FXML file \"" + resourceName + "\":" + e.getMessage());
        }

    }

}

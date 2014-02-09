package application.uw.cse.mag.appliancereader;

import application.Main;
import application.uw.cse.mag.appliancereader.util.FXMLPaneLoader;
import application.uw.cse.mag.appliancereader.views.CompleteParametersView;
import application.uw.cse.mag.appliancereader.views.ResultView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The MainView of this Application.  Presents users with the ability to
 *
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class MainView extends VBox {

    @FXML
    protected ResourceBundle resources;

    @FXML
    protected URL location;

    @FXML
    protected BorderPane contentBorderPane, overallBorderPane;

    @FXML
    protected ImageView image1, image2;

    @FXML
    protected Button image1Button, image2Button;

    @FXML
    protected VBox imagePane;

    @FXML
    protected MenuBar menuBar;

    private Image img1, img2;

    /**
     * File chooser for selecting images.
     */
    private final FileChooser chooser;

    /**
     *
     */
    private final MainViewListener listener;

    /**
     * Creates a MainView without defaulted with it initial values.
     */
    public MainView(MainViewListener listener) {
        if (listener == null)
            throw new NullPointerException(getClass().getSimpleName() + "(), " +
                    "Can't have Null Listener.");
        this.listener = listener;
        String resourceName = "Main.fxml";
        FXMLPaneLoader.load(this, resourceName);

        chooser = createFileChooser();
    }

    public void setParameters(CompleteParametersView view) {
        if (view == null) throw new IllegalArgumentException("View cannot be null.");
        overallBorderPane.setRight(view);
    }

    public void setResultView(ResultView view) {
        if (view == null) throw new IllegalArgumentException("View cannot be null.");
        contentBorderPane.setCenter(view);
    }

    @FXML
    void chooseImage1(ActionEvent event) {
        try {
            img1 = getImage("Choose Image 1");
            listener.onImagesUpdated(img1, img2);
            image1.setImage(img1);
        } catch (ImageUploadException e) {
            listener.onError(e.getMessage());
        }
    }

    @FXML
    void chooseImage2(ActionEvent event) {
        try {
            img2 = getImage("Choose Image 2");
            listener.onImagesUpdated(img1, img2);
            image2.setImage(img2);
        } catch (ImageUploadException e) {
            listener.onError(e.getMessage());
        }
    }

    @FXML
    void initialize() {
        assert contentBorderPane != null : "fx:id=\"contentBorderPane\" was not injected: check your FXML file 'Main.fxml'.";
        assert image1 != null : "fx:id=\"image1\" was not injected: check your FXML file 'Main.fxml'.";
        assert image1Button != null : "fx:id=\"image1Button\" was not injected: check your FXML file 'Main.fxml'.";
        assert image2 != null : "fx:id=\"image2\" was not injected: check your FXML file 'Main.fxml'.";
        assert image2Button != null : "fx:id=\"image2Button\" was not injected: check your FXML file 'Main.fxml'.";
        assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file 'Main.fxml'.";
        assert overallBorderPane != null : "fx:id=\"overallBorderPane\" was not injected: check your FXML file 'Main.fxml'.";
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Private Helper Methods.
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * Uses File Chooser Image.
     *
     * @param chooserTitle Title of the File Chooser.
     * @return Image.
     * @throws ImageUploadException
     */
    private Image getImage(String chooserTitle) throws ImageUploadException {
        chooser.setTitle(chooserTitle);
        Window stage = getScene().getWindow();
        File imgFile = chooser.showOpenDialog(stage);
        if (imgFile == null) return null;
        Image image;
        try {
            image = new Image(new FileInputStream(imgFile), Main.IMAGE_WIDTH, Main.IMAGE_HEIGHT,
                    true, true);
        } catch (IOException e) {
            throw new ImageUploadException("Unable to load image: " + e.getMessage());
        }
        return image;
    }

    /**
     * @return File Chooser for getting images.
     */
    private FileChooser createFileChooser() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"));
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        return chooser;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Interface to notify listeners that images have been uploaded.
    ////////////////////////////////////////////////////////////////////////////////////

    public interface MainViewListener {

        /**
         * Notifies that
         *
         * @param image1 First Image.
         * @param image2 Second Image.
         */
        public void onImagesUpdated(Image image1, Image image2);

        /**
         * Notifies listener that an error occured.
         *
         * @param error error message
         */
        public void onError(String error);

    }

    private static class ImageUploadException extends Exception {

        ImageUploadException(String msg) {
            super(msg);
        }

    }

}

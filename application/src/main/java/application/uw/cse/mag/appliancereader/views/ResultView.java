package application.uw.cse.mag.appliancereader.views;

import application.uw.cse.mag.appliancereader.util.FXMLPaneLoader;
import boofcv.struct.geo.AssociatedPair;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.ejml.data.DenseMatrix64F;
import uw.cse.mag.appliancereader.lib.cv.DetectionResultArgument;
import uw.cse.mag.appliancereader.lib.cv.HomographyCalculator;
import uw.cse.mag.appliancereader.lib.cv.ImagePointAssociator;
import uw.cse.mag.appliancereader.lib.ui.AssociateImageDrawer;
import uw.cse.mag.appliancereader.lib.ui.FeatureDrawer;
import uw.cse.mag.appliancereader.lib.ui.PerspectiveWarp;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by mhotan_dev on 1/2/14.
 */
public class ResultView extends BorderPane implements HomographyCalculator.OnHomographyChangedListener {

    private static final String RESOURCE_NAME = "ResultView.fxml";
    private static final String FEATURE_LABEL = "Feature Results\nNumber of left image features: %d\n" +
            "Number of right image features: %d";
    private static final String ALL_MATCHES_LABEL = "All Matches\nNumber of Matches: %d";
    private static final String INLIER_MATCHES_LABEL = "Inlier Matches\nNumber of Matches: %d";

    @FXML
    protected ResourceBundle resources;

    @FXML
    protected URL location;

    @FXML
    protected ImageView allMatchesImage, inlierMatchesImage, otherFeatureImage,
            referenceFeatureImage, resOtherImg, resRefImg, warpedImg;

    @FXML
    private Label allMatchesLabel, featureLabel, inlierMatchesLabel;

    @FXML
    private BorderPane allMatchesPane, featurePane, homographyPane, inlierMatchesPane;

    @FXML
    private VBox contentPane;

    @FXML
    private HBox inputImages;

    /**
     * The Image Point Associator
     */
    private HomographyCalculator calculator;

    /**
     * Result View for this
     */
    public ResultView(HomographyCalculator calculator) {
        if (calculator == null)
            throw new IllegalArgumentException(getClass().getSimpleName() + "(), " +
                    "Homography Calculator cannot be null");
        this.calculator = calculator;
        this.calculator.addListener(this);

        // Load the FXML layout.
        FXMLPaneLoader.load(this, RESOURCE_NAME);

        // Associate visbility with layout.
        featurePane.managedProperty().bind(featurePane.visibleProperty());
        allMatchesPane.managedProperty().bind(featurePane.visibleProperty());
        homographyPane.managedProperty().bind(featurePane.visibleProperty());
        inlierMatchesPane.managedProperty().bind(featurePane.visibleProperty());

        // Hide all the panes.
        featurePane.setVisible(false);
        allMatchesPane.setVisible(false);
        homographyPane.setVisible(false);
        inlierMatchesPane.setVisible(false);
    }

    @Override
    public void onHomographyChanged(DenseMatrix64F newHomography, DenseMatrix64F oldHomography) {
        update();
    }

    private void update() {
        ImagePointAssociator  ipa = calculator.getImageAssociator();
        BufferedImage refBuffered = calculator.getReferenceImage();
        BufferedImage otherBuffered = calculator.getOtherImage();
        if (ipa == null)
            throw new IllegalStateException("Image Point Associator cannot be null");
        if (refBuffered == null || otherBuffered == null)
            throw new IllegalStateException("Images cannot be null and a homography is produced.");

        DetectionResultArgument refFeature = ipa.getReferenceImageFeatures();
        DetectionResultArgument otherFeature = ipa.getOtherImageFeatures();

        // The feature
        if (refFeature == null || otherFeature == null)
            throw new IllegalStateException("Detection feature results cannot be null.");

        // Draw the images for features.
        referenceFeatureImage.setImage(SwingFXUtils.toFXImage(
                FeatureDrawer.drawFeatures(refBuffered, refFeature.getPoints()), null));
        otherFeatureImage.setImage(SwingFXUtils.toFXImage(
                FeatureDrawer.drawFeatures(otherBuffered, otherFeature.getPoints()), null));
        featureLabel.setText(String.format(Locale.getDefault(),
                FEATURE_LABEL,
                refFeature.getDescriptors().size(),
                otherFeature.getDescriptors().size()));
        featurePane.setVisible(true);

        // Draw all matching features.
        List<AssociatedPair> matches = ipa.getMatches();
        Image allMatchesImage = SwingFXUtils.toFXImage(
                AssociateImageDrawer.drawImage(refBuffered, otherBuffered, matches), null);
        this.allMatchesImage.setImage(allMatchesImage);
        allMatchesLabel.setText(String.format(Locale.getDefault(), ALL_MATCHES_LABEL, matches.size()));
        allMatchesPane.setVisible(true);

        // Draw the inlier matches
        List<AssociatedPair> inliers = calculator.getInliers();
        Image inlierMatchesImage = SwingFXUtils.toFXImage(
                AssociateImageDrawer.drawImage(refBuffered, otherBuffered, inliers), null);
        this.inlierMatchesImage.setImage(inlierMatchesImage);
        inlierMatchesLabel.setText(String.format(Locale.getDefault(), INLIER_MATCHES_LABEL, inliers.size()));
        inlierMatchesPane.setVisible(true);

        // Draw the homographic transform.
        DenseMatrix64F homography = calculator.getHomography();
        BufferedImage warpedImage = PerspectiveWarp.warp(otherBuffered, homography);
        resRefImg.setImage(SwingFXUtils.toFXImage(refBuffered, null));
        resOtherImg.setImage(SwingFXUtils.toFXImage(otherBuffered, null));
        warpedImg.setImage(SwingFXUtils.toFXImage(warpedImage, null));
        homographyPane.setVisible(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////    Private Helper Methods.
    ////////////////////////////////////////////////////////////////////////////////////


    @FXML
    void initialize() {
        assert allMatchesImage != null : "fx:id=\"allMatchesImage\" was not injected: check your FXML file 'Template.fxml'.";
        assert allMatchesLabel != null : "fx:id=\"allMatchesLabel\" was not injected: check your FXML file 'Template.fxml'.";
        assert allMatchesPane != null : "fx:id=\"allMatchesPane\" was not injected: check your FXML file 'Template.fxml'.";
        assert contentPane != null : "fx:id=\"contentPane\" was not injected: check your FXML file 'Template.fxml'.";
        assert featureLabel != null : "fx:id=\"featureLabel\" was not injected: check your FXML file 'Template.fxml'.";
        assert featurePane != null : "fx:id=\"featurePane\" was not injected: check your FXML file 'Template.fxml'.";
        assert homographyPane != null : "fx:id=\"homographyPane\" was not injected: check your FXML file 'Template.fxml'.";
        assert inlierMatchesImage != null : "fx:id=\"inlierMatchesImage\" was not injected: check your FXML file 'Template.fxml'.";
        assert inlierMatchesLabel != null : "fx:id=\"inlierMatchesLabel\" was not injected: check your FXML file 'Template.fxml'.";
        assert inlierMatchesPane != null : "fx:id=\"inlierMatchesPane\" was not injected: check your FXML file 'Template.fxml'.";
        assert inputImages != null : "fx:id=\"inputImages\" was not injected: check your FXML file 'Template.fxml'.";
        assert otherFeatureImage != null : "fx:id=\"otherFeatureImage\" was not injected: check your FXML file 'Template.fxml'.";
        assert referenceFeatureImage != null : "fx:id=\"referenceFeatureImage\" was not injected: check your FXML file 'Template.fxml'.";
        assert resOtherImg != null : "fx:id=\"resOtherImg\" was not injected: check your FXML file 'Template.fxml'.";
        assert resRefImg != null : "fx:id=\"resRefImg\" was not injected: check your FXML file 'Template.fxml'.";
        assert warpedImg != null : "fx:id=\"warpedImg\" was not injected: check your FXML file 'Template.fxml'.";


    }


}

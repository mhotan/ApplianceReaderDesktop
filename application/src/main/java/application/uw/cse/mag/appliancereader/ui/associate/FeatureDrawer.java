package application.uw.cse.mag.appliancereader.ui.associate;

import georegression.struct.point.Point2D_F64;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by mhotan_dev on 1/2/14.
 */
public class FeatureDrawer {

    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;

    public static Image drawFeatures(Image img, List<Point2D_F64> points) {
        if (img == null)
            throw new IllegalArgumentException("Image cannot be null");
        if (points == null)
            throw new IllegalArgumentException("Points cannot be null");

        BufferedImage image = SwingFXUtils.fromFXImage(img, null);
        BufferedImage result = uw.cse.mag.appliancereader.lib.ui.FeatureDrawer.drawFeatures(image, points);
        return SwingFXUtils.toFXImage(result, null);
    }



}

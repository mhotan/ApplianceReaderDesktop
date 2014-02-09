package application.uw.cse.mag.appliancereader.ui.associate;

import boofcv.struct.geo.AssociatedPair;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by mhotan_dev on 1/2/14.
 */
public final class AssociateImageDrawer {

    /**
     * Draws the association between the two images.
     *
     * @param image1 The first image.
     * @param image2 The second image
     * @param matches The matches between the images.
     * @return A Buffered image that shows the associated points
     */
    public static BufferedImage drawImage(Image image1,
                                          Image image2,
                                          List<AssociatedPair> matches) {

        BufferedImage img1 =  SwingFXUtils.fromFXImage(image1, null);
        BufferedImage img2 =  SwingFXUtils.fromFXImage(image2, null);
        return uw.cse.mag.appliancereader.lib.ui.AssociateImageDrawer.drawImage(img1, img2, matches);
    }

}

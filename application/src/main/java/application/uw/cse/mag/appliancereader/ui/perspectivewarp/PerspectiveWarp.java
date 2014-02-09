package application.uw.cse.mag.appliancereader.ui.perspectivewarp;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.ejml.data.DenseMatrix64F;

import java.awt.image.BufferedImage;

/**
 * Class that handles the perspective warping of images using distortion.
 */
public final class PerspectiveWarp {

    /**
     * Warps the image based off the given homography.
     *
     * @param image Image to warp using distortion
     * @param H used to define the distortion between images
     * @return The warped image.
     */
    public static Image perspectiveWarp(Image image, DenseMatrix64F H) {
        if (image == null)
            throw new NullPointerException("perspectiveWarp(), Image reference cannot be null");
        BufferedImage input = SwingFXUtils.fromFXImage(image, null);
        return SwingFXUtils.toFXImage(uw.cse.mag.appliancereader.
                lib.ui.PerspectiveWarp.warp(input, H), null);
    }

}

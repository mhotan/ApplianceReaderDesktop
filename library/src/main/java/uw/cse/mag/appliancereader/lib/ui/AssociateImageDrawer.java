package uw.cse.mag.appliancereader.lib.ui;

import boofcv.struct.geo.AssociatedPair;
import georegression.struct.point.Point2D_F64;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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
    public static BufferedImage drawImage(BufferedImage image1,
                                          BufferedImage image2,
                                          List<AssociatedPair> matches) {
        int canvasWidth = image1.getWidth() + image2.getWidth();
        int canvasHeight = Math.max(image1.getHeight(), image2.getHeight());
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw the first image.
        Image img1 = SwingFXUtils.toFXImage(image1, null);
        gc.drawImage(img1, 0, 0);

        // Draw the second image. with the width offset of the first image
        Image img2 = SwingFXUtils.toFXImage(image2, null);
        gc.drawImage(img2, img1.getWidth(), 0);

        // set the parameters for the line.
        gc.setLineWidth(3.0);
        gc.setStroke(Color.GREEN);
        for (AssociatedPair match : matches) {
            Point2D_F64 p1 = match.getP1();
            Point2D_F64 p2 = match.getP2();
            // Draw the line from p1 to p2.
            // The images are side by side so
            gc.strokeLine(p1.getX(), p1.getY(), img1.getWidth() + p2.getX(), p2.getY());
        }

        // Return the AWT image of this
        return SwingFXUtils.fromFXImage(canvas.snapshot(new SnapshotParameters(), null), null);
    }

}

package uw.cse.mag.appliancereader.lib.ui;

import georegression.struct.point.Point2D_F64;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by mhotan_dev on 1/2/14.
 */
public class FeatureDrawer {

    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;

    public static BufferedImage drawFeatures(BufferedImage img, List<Point2D_F64> points) {
        if (img == null)
            throw new IllegalArgumentException("Image cannot be null");
        if (points == null)
            throw new IllegalArgumentException("Points cannot be null");

        BufferedImage copy = ImageOps.deepCopy(img);
        for (Point2D_F64 p: points) {
            drawPoint(copy, p, Color.RED);
        }
        return copy;
    }

    /**
     * Draws a single Point
     * @param image Image to draw point on.
     * @param point Point to draw
     * @param color Color to use.
     */
    public static void drawPoint(BufferedImage image, Point2D_F64 point, Color color) {
        if (image == null)
            throw new IllegalArgumentException("Image cannot be null");
        if (point == null)
            throw new IllegalArgumentException("Point cannot be null");
        if (color == null)
            throw new IllegalArgumentException("Color cannot be null");
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.drawOval((int)point.getX() - WIDTH / 2, (int)point.getY() - HEIGHT / 2, WIDTH, HEIGHT);
    }

}

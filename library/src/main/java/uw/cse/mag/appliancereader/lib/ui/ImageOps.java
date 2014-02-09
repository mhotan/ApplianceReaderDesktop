package uw.cse.mag.appliancereader.lib.ui;


import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by mhotan_dev on 1/2/14.
 */
public final class ImageOps {

    /**
     * Provides an exact duplication copy of the source image
     *
     * @param source Image to copy.
     * @return Copy of the image.
     */
    public static BufferedImage deepCopy(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
}

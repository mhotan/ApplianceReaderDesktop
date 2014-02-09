package uw.cse.mag.appliancereader.lib.ui;

import boofcv.alg.distort.DistortImageOps;
import boofcv.alg.distort.PointToPixelTransform_F32;
import boofcv.alg.distort.PointTransformHomography_F32;
import boofcv.alg.interpolate.TypeInterpolate;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.struct.distort.PixelTransform_F32;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.MultiSpectral;
import org.ejml.data.DenseMatrix64F;

import java.awt.image.BufferedImage;

/**
 * Class that handles the perspective warping of images using distortion.
 */
public final class PerspectiveWarp {

    /**
     * Warps the input image based
     *
     * @param image Image to warp using distortion
     * @param H used to define the distortion between images
     * @return The warped Image.
     */
    public static BufferedImage warp(BufferedImage image, DenseMatrix64F H) {
        if (image == null)
            throw new NullPointerException("perspectiveWarp(), Image reference cannot be null");
        if (H == null)
            throw new NullPointerException("perspectiveWarp(), Homography matrix cannot be null");

        // get the RGB version of the image.
        image = getRGB(image);

        MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(image, null, ImageFloat32.class);
        // Make sure the bands are in the correct order.
        ConvertBufferedImage.orderBandsIntoRGB(input, image);

        // Create the output file that is the same size of the original image.
        MultiSpectral<ImageFloat32> output = input._createNew(input.getWidth(), input.getHeight());

        // Create the transform for distorting the image
        PointTransformHomography_F32 homography = new PointTransformHomography_F32(H);
        PixelTransform_F32 pixelTransform = new PointToPixelTransform_F32(homography);

        // Apply distortion and show the results
        DistortImageOps.distortMS(input, output, pixelTransform, true, TypeInterpolate.BILINEAR);
        return ConvertBufferedImage.convertTo_F32(output, null);
    }

    /**
     * Returns an RGB version of the argument image.
     *
     * @param image Image to copy to RGB
     * @return The Buffered Image to copy
     */
    private static BufferedImage getRGB(BufferedImage image) {
        if (image.getType() == BufferedImage.TYPE_INT_RGB)
            return image;
        // create an image in the format you need and then draw the original image onto it.
        BufferedImage rgb = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        rgb.getGraphics().drawImage(image, 0, 0, null);
        return rgb;
    }

}

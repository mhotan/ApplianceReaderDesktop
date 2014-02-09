package uw.cse.mag.appliancereader.lib.cv;

import boofcv.struct.FastQueue;
import boofcv.struct.feature.SurfFeature;
import boofcv.struct.image.ImageFloat32;
import georegression.struct.point.Point2D_F64;

import java.util.List;

/**
 * Return arguments for Feature Detection.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class DetectionResultArgument {

    private final FastQueue<SurfFeature> descriptors;
    private final List<Point2D_F64> points;
    private final ImageFloat32 image;

    /**
     * Create a bundle for return arguments.  Every point in points corresponds to the feature
     * descriptors.
     *
     * @param descriptors Feature Descriptors
     * @param points Feature center points.
     * @param image Image with features
     */
    DetectionResultArgument(FastQueue<SurfFeature> descriptors,
                            List<Point2D_F64> points,
                            ImageFloat32 image) {
        if (descriptors == null)
            throw new NullPointerException(getClass().getSimpleName() + "() Null descriptors");
        if (points == null)
            throw new NullPointerException(getClass().getSimpleName() + "() Null points");
        if (image == null)
            throw new NullPointerException(getClass().getSimpleName() + "() Null image");
        this.descriptors = descriptors;
        this.points = points;
        this.image = image;
    }

    /**
     * @return Associated Descriptors
     */
    public FastQueue<SurfFeature> getDescriptors() {
        return descriptors;
    }

    /**
     * @return Associated Points
     */
    public List<Point2D_F64> getPoints() {
        return points;
    }

    /**
     * @return Associated reference image.
     */
    public ImageFloat32 getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "DetectionResultArgument{" +
                "descriptors=" + descriptors +
                ", points=" + points +
                ", image=" + image +
                '}';
    }
}

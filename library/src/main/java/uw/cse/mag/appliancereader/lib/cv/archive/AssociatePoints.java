package uw.cse.mag.appliancereader.lib.cv.archive;

import boofcv.abst.feature.associate.AssociateDescription;
import boofcv.abst.feature.associate.ScoreAssociation;
import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.abst.feature.detect.interest.ConfigFastHessian;
import boofcv.alg.feature.UtilFeature;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.feature.associate.FactoryAssociation;
import boofcv.factory.feature.detdesc.FactoryDetectDescribe;
import boofcv.gui.feature.AssociationPanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.FastQueue;
import boofcv.struct.feature.SurfFeature;
import boofcv.struct.feature.TupleDesc;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSingleBand;
import georegression.struct.point.Point2D_F64;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
* After interest points have been detected in two images the next step is to associate the two
* sets of images so that the relationship can be found.  This is done by computing descriptors for
* each detected feature and associating them together.  In the code below abstracted interfaces are
* used to allow different algorithms to be easily used.  The cost of this abstraction is that detector/descriptor
* specific information is thrown away, potentially slowing down or degrading performance.
*
* @author Peter Abeles
*/
public class AssociatePoints<T extends ImageSingleBand, TD extends TupleDesc> {

    public static final Class IMAGE_TYPE = ImageFloat32.class;

    // algorithm used to detect and describe interest points
    DetectDescribePoint<T, TD> detDesc;
    // Associated descriptions together by minimizing an error metric
    AssociateDescription<TD> associate;

    // location of interest points
    List<Point2D_F64> pointsA;
    List<Point2D_F64> pointsB;

    // Reference to image type.
    Class<T> imageType;


    public AssociatePoints(DetectDescribePoint<T, TD> detDesc,
                           AssociateDescription<TD> associate) {
        this(detDesc, associate, IMAGE_TYPE);
    }

    /**
     * Creates
     *
     * @param detDesc
     * @param associate
     * @param imageType
     */
    public AssociatePoints(DetectDescribePoint<T, TD> detDesc,
                           AssociateDescription<TD> associate,
                           Class<T> imageType) {
        this.detDesc = detDesc;
        this.associate = associate;
        this.imageType = imageType;
    }

    /**
     * Detect and associate point features in the two images.  Display the results.
     */
    public void associate( BufferedImage imageA , BufferedImage imageB )
    {
        T inputA = ConvertBufferedImage.convertFromSingle(imageA, null, imageType);
        T inputB = ConvertBufferedImage.convertFromSingle(imageB, null, imageType);

        // stores the location of detected interest points
        pointsA = new ArrayList<Point2D_F64>();
        pointsB = new ArrayList<Point2D_F64>();

        // stores the description of detected interest points
        FastQueue<TD> descA = UtilFeature.createQueue(detDesc, 100);
        FastQueue<TD> descB = UtilFeature.createQueue(detDesc, 100);

        // describe each image using interest points
        describeImage(inputA,pointsA,descA);
        describeImage(inputB,pointsB,descB);

        // Associate features between the two images
        associate.setSource(descA);
        associate.setDestination(descB);
        associate.associate();

        // display the results
        AssociationPanel panel = new AssociationPanel(20);
        panel.setAssociation(pointsA,pointsB,associate.getMatches());
        panel.setImages(imageA,imageB);

        ShowImages.showWindow(panel, "Associated Features");
    }

    /**
     * Detects features inside the two images and computes descriptions at those points.
     */
    private void describeImage(T input, List<Point2D_F64> points, FastQueue<TD> descs )
    {
        detDesc.detect(input);
        for( int i = 0; i < detDesc.getNumberOfFeatures(); i++ ) {
            points.add( detDesc.getLocation(i).copy() );
            descs.grow().setTo(detDesc.getDescription(i));
        }
    }

    public static void main( String args[] ) {

        Class imageType = ImageFloat32.class;

        // select which algorithms to use
        DetectDescribePoint<ImageFloat32, SurfFeature> detDesc = FactoryDetectDescribe.surfStable(
                new ConfigFastHessian(1, 2, 200, 1, 9, 4, 4), null, null, imageType);

        ScoreAssociation scorer = FactoryAssociation.defaultScore(detDesc.getDescriptionType());
        AssociateDescription associate = FactoryAssociation.greedy(scorer, Double.MAX_VALUE, true);

        // load and match images
        AssociatePoints<ImageFloat32, SurfFeature> app =
                new AssociatePoints<ImageFloat32, SurfFeature>(detDesc,associate,imageType);

        String dir = "/Users/mhotan_dev/Dropbox/";
        BufferedImage imageA = UtilImageIO.loadImage(dir + "1.jpg");
        BufferedImage imageB = UtilImageIO.loadImage(dir + "2.jpg");

        app.associate(imageA,imageB);
    }

}

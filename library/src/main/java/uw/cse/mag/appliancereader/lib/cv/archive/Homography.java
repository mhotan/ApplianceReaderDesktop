package uw.cse.mag.appliancereader.lib.cv.archive;

import boofcv.abst.feature.associate.AssociateDescription;
import boofcv.abst.feature.associate.ScoreAssociation;
import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.abst.feature.detect.interest.ConfigFastHessian;
import boofcv.abst.geo.Estimate1ofEpipolar;
import boofcv.abst.geo.fitting.DistanceFromModelResidualN;
import boofcv.abst.geo.fitting.GenerateEpipolarMatrix;
import boofcv.alg.distort.DistortImageOps;
import boofcv.alg.distort.PointToPixelTransform_F32;
import boofcv.alg.distort.PointTransformHomography_F32;
import boofcv.alg.geo.h.HomographyResidualSampson;
import boofcv.alg.interpolate.TypeInterpolate;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.feature.associate.FactoryAssociation;
import boofcv.factory.feature.detdesc.FactoryDetectDescribe;
import boofcv.factory.geo.FactoryMultiView;
import boofcv.gui.feature.AssociationPanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.FastQueue;
import boofcv.struct.distort.PixelTransform_F32;
import boofcv.struct.feature.AssociatedIndex;
import boofcv.struct.feature.SurfFeature;
import boofcv.struct.geo.AssociatedPair;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.MultiSpectral;
import org.ddogleg.fitting.modelset.ModelMatcher;
import org.ddogleg.fitting.modelset.ransac.Ransac;
import org.ejml.data.DenseMatrix64F;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Homography {

    /**
     *
     */
    private static final int RANDOMIZE_SEED = (int) Math.random() + 100000;

    /**
     * Given a set of noisy observations, compute the Homography matrix.
     *
     * @param matches List of associated features between the two images
     * @param inliers List of feature pairs that were determined to not be noise.
     * @return The found Homography matrix.
     */
    public static DenseMatrix64F robustHomography(List<AssociatedPair> matches,
                                                  List<AssociatedPair> inliers) {
        // Select which linear algorithm is to be used.
        // Currently we will use Homography to match different perspective views.
        Estimate1ofEpipolar computeHomography = FactoryMultiView.computeHomography(true);

        // Wrapper so that this estimator can be used by the robust estimator
        GenerateEpipolarMatrix generateH = new GenerateEpipolarMatrix(computeHomography);

        // How the error is Measured
        // Tailored to use
        DistanceFromModelResidualN<DenseMatrix64F, AssociatedPair> errorMetric =
                new DistanceFromModelResidualN<DenseMatrix64F, AssociatedPair>(
                        new HomographyResidualSampson());

        // Use RANSAC to estimate the Fundamental matrix
        ModelMatcher<DenseMatrix64F,AssociatedPair> robustH =
                new Ransac<DenseMatrix64F, AssociatedPair>(
                        123123, generateH, errorMetric, 1000, 1.0);

        // Estimate the homography matrix while removing outliers.
        if (!robustH.process(matches)) {
            throw new IllegalArgumentException("Failed to find matches via Homography!");
        }
        inliers.addAll(robustH.getMatchSet());

        // Process the homography based off the matches.
        DenseMatrix64F H = new DenseMatrix64F(3, 3);
        computeHomography.process(inliers, H);

//        DenseMatrix64F H = new DenseMatrix64F(3,3);
//        GeoModelRefine<DenseMatrix64F, AssociatedPair> refine =
//                FactoryMultiView.refineHomography(1e-8, 400, EpipolarError.SAMPSON);
//        if (!refine.process(robustH.getModel(), inliers, H))
//            throw new IllegalArgumentException("Unable to refine matches from Homography!");

        return H;
    }

    /**
     * Use the associate point feature example to create a list of {@link AssociatedPair} for use in computing the
     * fundamental matrix.
     */
    public static List<AssociatedPair> computeMatches( BufferedImage left , BufferedImage right ) {
        DetectDescribePoint detDesc = FactoryDetectDescribe.surfStable(
                new ConfigFastHessian(1, 2, 200, 1, 9, 4, 4), null, null, ImageFloat32.class);

        ScoreAssociation<SurfFeature> scorer = FactoryAssociation.scoreEuclidean(detDesc.getDescriptionType(), true);
        AssociateDescription<SurfFeature> associate = FactoryAssociation.greedy(scorer, 1, true);

        AssociatePoints<ImageFloat32,SurfFeature> findMatches =
                new AssociatePoints<ImageFloat32,SurfFeature>
                        (detDesc, associate, ImageFloat32.class);

        findMatches.associate(left,right);

        List<AssociatedPair> matches = new ArrayList<AssociatedPair>();
        FastQueue<AssociatedIndex> matchIndexes = associate.getMatches();

        // Compile the associated points between the images.
        for( int i = 0; i < matchIndexes.size; i++ ) {
            AssociatedIndex a = matchIndexes.get(i);
            AssociatedPair p = new AssociatedPair(findMatches.pointsA.get(a.src) , findMatches.pointsB.get(a.dst));
            matches.add(p);
        }

        return matches;
    }

    /**
     *
     * @param image
     * @param homography
     * @return
     */
    public static BufferedImage warp(BufferedImage image, DenseMatrix64F homography) {

        // load a color image
        MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(
                image, null, ImageFloat32.class);

        // Create a smaller output image for processing later on
        MultiSpectral<ImageFloat32> output = input._createNew(input.getWidth(), input.getHeight());

        // Create the transform for distorting the image
        PointTransformHomography_F32 H = new PointTransformHomography_F32(homography);
        PixelTransform_F32 pixelTransform = new PointToPixelTransform_F32(H);

        // Apply distortion and show the results
        DistortImageOps.distortMS(input, output, pixelTransform, true, TypeInterpolate.BILINEAR);

        BufferedImage flat = ConvertBufferedImage.convertTo_F32(output, null);
        return flat;
    }

    public static void main( String args[] ) {

        String dir = "/Users/mhotan_dev/Dropbox/appliancepics/";

        BufferedImage imageA = UtilImageIO.loadImage(dir + "1.jpg");
        BufferedImage imageB = UtilImageIO.loadImage(dir + "2.jpg");

        List<AssociatedPair> matches = computeMatches(imageA,imageB);

        // Where the fundamental matrix is stored
        DenseMatrix64F H;
        // List of matches that matched the model
        List<AssociatedPair> inliers = new ArrayList<AssociatedPair>();

        H = robustHomography(matches, inliers);
        System.out.println("Homography");
        H.print();

        int borderSize = 10;
        // display the inlier matches found using the robust estimator
        AssociationPanel panel = new AssociationPanel(borderSize);
        panel.setAssociation(inliers);
        panel.setImages(imageA,imageB);
        ShowImages.showWindow(panel, "Inlier Pairs");

//
//        AssociationPanel panel2 = new AssociationPanel(borderSize);
//        BufferedImage warped = warp(imageB, H);
//        panel2.setImages(imageA, warped);
//        ShowImages.showWindow(panel2, "Warped Image");
    }


}

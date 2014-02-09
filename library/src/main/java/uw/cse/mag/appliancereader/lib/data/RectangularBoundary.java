package uw.cse.mag.appliancereader.lib.data;

/**
 * A Rectangular Boundary.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class RectangularBoundary extends Boundary {

    /**
     * Internal fields of this boundary
     *
     * <b>/-------------------------------/</b>
     * <b>|                               |</b>
     * <b>|  (mX, mY)                     |</b>
     * <b>|     /-------------/           |</b>
     * <b>|     |             |           |</b>
     * <b>|     |             |mHeight    |</b>
     * <b>|     /-------------|           |</b>
     * <b>|          mWidth               |</b>
     * <b>/-------------------------------/</b>
     */
    private final int mX, mY, mWidth, mHeight;

    /**
     * Creates a rectangular boundary with the coordinate system the same of the standard image.
     * Therefore x and y correspond to the top left coordinate within the area it is in.
     *
     * @param x The farthest most left or smallest X value within the boundary.
     * @param y The highest or smallest Y value within the boundary.
     * @param width Width of the rectangular boundary
     * @param height Height of the rectangular boundary.
     */
    public RectangularBoundary(int x, int y, int width, int height) {
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
    }

    /**
     * @return The smallest X value for this boundary.
     */
    public int getX() {
        return mX;
    }

    /**
     * @return The smallest Y value for this boundary.
     */
    public int getY() {
        return mY;
    }

    /**
     * @return The width of the boundary.
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * @return The height of the boundary.
     */
    public int getHeight() {
        return mHeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RectangularBoundary)) return false;

        RectangularBoundary that = (RectangularBoundary) o;

        if (mHeight != that.mHeight) return false;
        if (mWidth != that.mWidth) return false;
        if (mX != that.mX) return false;
        if (mY != that.mY) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mX;
        result = 31 * result + mY;
        result = 31 * result + mWidth;
        result = 31 * result + mHeight;
        return result;
    }

}

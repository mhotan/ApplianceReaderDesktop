package uw.cse.mag.appliancereader.lib.data;

import java.nio.file.Path;

/**
 *
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public abstract class ImageSource {

    // Path to the image.
    private final Path mPath;

    /**
     * Creates an Image source
     * @param path
     */
    public ImageSource(Path path) {
        mPath = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageSource)) return false;
        ImageSource that = (ImageSource) o;
        if (!mPath.equals(that.mPath)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return mPath.hashCode();
    }

}

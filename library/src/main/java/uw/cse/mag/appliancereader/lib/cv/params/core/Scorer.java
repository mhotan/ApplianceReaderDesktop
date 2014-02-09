package uw.cse.mag.appliancereader.lib.cv.params.core;

/**
 * Created by mhotan_dev on 12/20/13.
 */
public enum Scorer {
    DEFAULT("Default"),
    EUCLIDEAN("Euclidean"),
    HAMMING("Hamming"),
    SAD("Sum of Absolute Difference");

    private final String mName;

    private Scorer(String name) {
        mName = name;
    }

    public String toString() {
        return mName;
    }

    public String getName() {
        return mName;
    }

}

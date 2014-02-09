package uw.cse.mag.appliancereader.lib.storage;

import uw.cse.mag.appliancereader.lib.data.Appliance;

import java.io.File;
import java.io.IOException;

/**
 * A directory that is used to contain the contents of exactly one appliance.

 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class ApplianceDirectory {

    /**
     * Directory where the files of the representing appliance.
     */
    private final File mDirectory;

    /**
     * Appliance to set this directory to.
     */
    private final Appliance mAppliance;

    /**
     * Creates a wrapper around the Directory that contains Appliance data.
     * If a directory does not exists then it is created.
     *
     * @param app Appliance to create directory for.
     */
    ApplianceDirectory(Appliance app, File parentDir) {
        if (parentDir == null)
            throw new NullPointerException(getClass().getSimpleName() + "() " +
                    "Parent Directory argument is null");
        if (app == null)
            throw new NullPointerException(getClass().getSimpleName() + "() " +
                    "Appliance to create directory is null");
        if (!parentDir.isDirectory())
            throw new IllegalArgumentException(getClass().getSimpleName() + "() " +
                    "Input file is not a directory.");

        mAppliance = app;
        String dirName = getDirName(mAppliance);

        // Create or reference the directory.
        mDirectory = new File(parentDir, dirName);
        try {
            // Create the file if it does not exist.
            if (!mDirectory.exists()) {
                mDirectory.createNewFile();

                // Create a directory if it is not already one.
                if (!mDirectory.isDirectory()) {
                    mDirectory.mkdir();
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException("Unable to create directory for this appliance.");
        }

        // Synchronize the state of this appliance with its file directory.
        sync();
    }

    /**
     * Checks whether this directory represents this appliance.
     *
     * @param app Appliance to check whether this directory has
     * @return Whether this directory if for the argument appliance.
     */
    public boolean represents(Appliance app) {
        return mAppliance.equals(app);
    }

    /**
     * Attempts to update the file directory for this appliance.
     *
     * @return Whether or not any state was updated.
     */
    public boolean sync() {
        // TODO Implement sync method.

        // Pull all the methods


        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!o.getClass().equals(getClass())) return false;
        ApplianceDirectory dir = (ApplianceDirectory) o;
        return mAppliance.equals(dir.mAppliance);
    }

    @Override
    public int hashCode() {
        return 3 * mAppliance.hashCode();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    ////    Private helper methods
    //////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the directory name associated to this appliance.
     *
     * @param appliance The appliance to create directory name for.
     * @return Alphanumeric name for the directory.
     */
    private static String getDirName(Appliance appliance) {
        return appliance.getMake() + "_" + appliance.getModel();
    }

}

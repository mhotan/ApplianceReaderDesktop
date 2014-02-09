package uw.cse.mag.appliancereader.lib.storage;

import uw.cse.mag.appliancereader.lib.data.Appliance;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Factory that does some extra book keeping for Appliance Directories.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
class ApplianceDirectoryFactory {

    /**
     * Private instance of this.
     */
    private static Collection<ApplianceDirectoryFactory> instances;

    /**
     * Collection of Appliance Directories.
     */
    private final Collection<ApplianceDirectory> mDirectories;

    /**
     *  The Main directory to associate with this factory.
     */
    private final File mMainDir;

    /**
     * Getter method for singleton instance.
     *
     * @return Instance of this Factory
     */
    public static ApplianceDirectoryFactory getFactory(File mainDir) {
        // Check if there
        if (instances == null) {
            instances = new ArrayList<ApplianceDirectoryFactory>();
        }
        //
        for (ApplianceDirectoryFactory factory : instances) {
            if (factory.mMainDir.equals(mainDir))
                return factory;
        }
        ApplianceDirectoryFactory factory = new ApplianceDirectoryFactory(mainDir);
        instances.add(factory);
        return factory;
    }

    /**
     * Creates a factory that tracks references to Appliance Directories.
     */
    private ApplianceDirectoryFactory(File mainDir) {
       mDirectories = new ArrayList<ApplianceDirectory>();
        mMainDir = mainDir;
    }

    /**
     * Gets the Appliance directory that corresponds to this appliance.
     *
     * @param appliance Appliance to get directory for.
     * @return The file directory of the argument appliance.
     */
    public ApplianceDirectory getDirectory(Appliance appliance) {

        // Check if we have already created the Appliance Directory.
        for (ApplianceDirectory appDir: mDirectories) {
            if (appDir.represents(appliance))
                return appDir;
        }

        // Return the directory of for this appliance.
        ApplianceDirectory dir = new ApplianceDirectory(appliance, mMainDir);
        mDirectories.add(dir);
        return dir;
    }

}

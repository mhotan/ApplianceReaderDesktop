package uw.cse.mag.appliancereader.lib.storage;

import uw.cse.mag.appliancereader.lib.data.Appliance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Correctly organizes appliance data within a specified subdirectory.
 * <b>
 * <b>For an Appliance "A" The layout class
 <b>
 <b>-ApplianceReader
 <b>--Appliances
 <b>---A
 <b>----TODO: Add meta data including the ID
 <b>----reference image directory
 <b>----other images (optional)
 <b>----XML file directory (Thing that describes all the features)
 * <b>
 * Thread safe class for file management Read and Write access
 * <b>This class is specific for Appliance Reader
 * storage
 * @author Michael Hotan
 */
public class FileDirectoryManager {

    /**
     * Main Directory for all the appliances and appliance directories.
     */
    private final File mMainDir;

    /**
     * Name of the directory
     */
    private static final String APPLIANCE_DIR = "Appliances";

    /**
     * Creates a file directory manager rooting all the Appliances with inputted directory.
     *
     * @param directory Directory where all the appliance are stored.
     */
    public FileDirectoryManager(File directory) {
        if (directory == null)
            throw new NullPointerException(getClass().getSimpleName() + "() Null Main Directory");
        if (!directory.isDirectory())
            throw new IllegalArgumentException(getClass().getSimpleName() + "() Input file is not a directory");
        mMainDir = directory;
    }

    /**
     * Creates a file directory manager rooting all the appliances at the directory
     * of the inputted path.
     *
     * @param dirPath Path to the main directory where all the Appliances will be stored
     */
    public FileDirectoryManager(Path dirPath) {
        if (dirPath == null)
            throw new NullPointerException(getClass().getSimpleName() + "() Null Path");
        try {
            Files.createDirectory(dirPath);
        } catch (IOException e) {
            throw new IllegalArgumentException("Illegal Path: " + dirPath);
        }
        mMainDir = dirPath.toFile();
    }

    /**
     * All the appliances are stored in the
     *
     * @return the Directory of all the Appliance Directories
     */
    private File getAppliancesDir() {
        File appliancesDir = new File(mMainDir, APPLIANCE_DIR);

        // Create the directory for all the appliances.
        try {
            if (appliancesDir.exists() && !appliancesDir.isDirectory())
                appliancesDir.delete();
            if (!appliancesDir.exists()) {
                appliancesDir.createNewFile();
                if (!appliancesDir.isDirectory())
                    appliancesDir.mkdir();
            }
        } catch (IOException e) {
            throw new IllegalStateException("getAppliancesDir() Unable to get directory for all the appliances");
        }
        return appliancesDir;
    }

    /**
     * The Directory for the argument appliance.
     *
     * @param appliance The appliance to get the directory of.
     * @return The directory of the Appliance.
     */
    public ApplianceDirectory getApplianceDirectory(Appliance appliance) {
        // Use the factory to be able to due some in memory caching.
        return ApplianceDirectoryFactory.getFactory(getAppliancesDir()).getDirectory(appliance);
    }

    /**
     * Adds an Appliance to this file directory.  Synchronizing the
     * contents of the Appliance to its directory.
     *
     * @param appliance Appliance to add
     */
    public void addApplianceDirectory(Appliance appliance) {
        // We need to simply make sure that we have a directory.
        ApplianceDirectory dir = getApplianceDirectory(appliance);
        // Then sync it with.
        dir.sync();
    }

}

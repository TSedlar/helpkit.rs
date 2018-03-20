package rs.helpkit.pref;

import java.io.File;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class HKConfig {

    public static final File HOME = new File(System.getProperty("user.home"), "RSHelpKit/");
    public static final File JAGEX = new File(HOME, "jagex/");
    public static final File DATA = new File(HOME, "data/");

    static { // make initial directories
        HOME.mkdirs();
        JAGEX.mkdirs();
        DATA.mkdirs();
    }

    /**
     * Gets the absolute path to the given file
     *
     * @param dir The parent directory of the file
     * @param file The file to retrieve a path for
     * @return The absolute path to the given file
     */
    public static String path(File dir, String file) {
        return new File(dir, file).getAbsolutePath();
    }

    /**
     * Gets the absolute path to the given file
     *
     * @param file The file to retrieve a path for
     * @return The absolute path to the given file
     */
    public static String path(String file) {
        return path(HOME, file);
    }
}

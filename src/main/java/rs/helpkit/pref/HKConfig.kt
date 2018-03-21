package rs.helpkit.pref

import java.io.File

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object HKConfig {

    val HOME = File(System.getProperty("user.home"), "RSHelpKit/")
    val JAGEX = File(HOME, "jagex/")
    val DATA = File(HOME, "data/")

    init { // make initial directories
        HOME.mkdirs()
        JAGEX.mkdirs()
        DATA.mkdirs()
    }

    /**
     * Gets the absolute path to the given file
     *
     * @param dir The parent directory of the file
     * @param file The file to retrieve a path for
     * @return The absolute path to the given file
     */
    fun path(dir: File, file: String): String {
        return File(dir, file).absolutePath
    }

    /**
     * Gets the absolute path to the given file
     *
     * @param file The file to retrieve a path for
     * @return The absolute path to the given file
     */
    fun path(file: String): String {
        return path(HOME, file)
    }
}

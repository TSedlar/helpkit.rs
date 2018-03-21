package rs.helpkit.util.io

import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object Internet {

    /**
     * Downloads the remote file to the given target
     *
     * @param url The url to download from
     * @param targetFile The file to download to
     * @return <tt>true</tt> if the download was successful, otherwise <tt>false</tt>.
     */
    fun download(url: String, targetFile: String): Boolean {
        try {
            URL(url).openStream().use { input ->
                Files.copy(input, Paths.get(targetFile), StandardCopyOption.REPLACE_EXISTING)
                return true
            }
        } catch (e: Exception) {
            return false
        }
    }
}

package rs.helpkit.util.io;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class Internet {

    /**
     * Downloads the remote file to the given target
     *
     * @param url The url to download from
     * @param targetFile The file to download to
     * @return <tt>true</tt> if the download was successful, otherwise <tt>false</tt>.
     */
    public static boolean download(String url, String targetFile) {
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, Paths.get(targetFile), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

package rs.helpkit;

import rs.helpkit.pref.HKConfig;

import java.io.File;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class RSHelpKit {

    public static void main(String[] args) throws Throwable {
        String viewerJar = HKConfig.path(HKConfig.JAGEX, "jagexappletviewer.jar");
        try {
            JagexAppletViewer.download(viewerJar);
        } catch (Exception e) {
            System.out.println("Failed to download new jagexappletviewer.jar..");
        }
        if (new File(viewerJar).exists()) {
            JagexAppletViewer.run(viewerJar);
        }
    }
}

package rs.helpkit;

import rs.helpkit.pref.RSPreferences;
import rs.helpkit.reflect.Classes;
import rs.helpkit.util.io.Internet;

import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class JagexAppletViewer {

    private static final String VIEWER_URL = "http://oldschool.runescape.com/downloads/jagexappletviewer.jar";
    private static final String CONFIG = "http://oldschool%s.runescape.com/l=en/jav_config.ws";

    @SuppressWarnings("deprecation") // Applet is deprecated in Java9
    private static Field findAppletField(ClassLoader loader) throws ClassNotFoundException {
        Class<?> viewer = loader.loadClass("app.appletviewer");
        if (viewer == null) {
            throw new IllegalStateException("app.appletviewer not found");
        }
        Field applet = Classes.findField(viewer, field -> field.getType().equals(Applet.class));
        if (applet == null) {
            throw new IllegalStateException("Applet not found");
        }
        return applet;
    }

    @SuppressWarnings("deprecation") // Applet is deprecated in Java9
    private static Applet findRSApplet(ClassLoader loader) throws ClassNotFoundException, IllegalAccessException,
            InterruptedException {
        Field appletField = findAppletField(loader);

        Object applet;

        int tries = 0;

        while ((applet = appletField.get(null)) == null) { // check field value for non-null
            if (tries++ >= 10) { // something is clearly wrong is the applet hasn't loaded
                System.err.println("Unable to locate RS applet");
                System.exit(0);
            }
            Thread.sleep(1000);
        }

        return (Applet) applet;
    }

    public static void download(String targetFile) throws IOException {
        boolean outdated = true;

        File jar = new File(targetFile);

        if (jar.exists()) {
            int localHash = new JarFile(jar).getManifest().hashCode();
            String jarManifestURL = ("jar:" + VIEWER_URL + "!/META-INF/MANIFEST.MF");
            JarURLConnection remoteViewer = (JarURLConnection) new URL(jarManifestURL).openConnection();
            int remoteHash = remoteViewer.getManifest().hashCode();
            if (localHash == remoteHash) { // The local and remote manifest hashcode match, already up-to-date
                outdated = false;
            }
        }

        if (outdated) {
            // TODO: show download progress of jagexappletviewer.jar
            Internet.download(VIEWER_URL, jar.getAbsolutePath());
        } else {
            // TODO: show that jagexappletviewer.jar already up-to-date
        }
    }

    @SuppressWarnings("deprecation") // Applet is deprecated in Java9
    public static OSRSContainer run(String targetFile) throws Throwable {
        File jar = new File(targetFile);
        URLClassLoader loader = URLClassLoader.newInstance(new URL[]{jar.toURI().toURL()});

        Class<?> main = loader.loadClass("jagexappletviewer");
        Method exec = main.getDeclaredMethod("main", String[].class);

        System.setProperty("com.jagex.config", String.format(CONFIG, RSPreferences.getDefaultWorld()));
        System.setProperty("sun.java2d.nodraw", "true");

        MethodHandle mainHandle = MethodHandles.lookup().unreflect(exec);
        String[] viewerArgs = {"./"};
        mainHandle.invoke((Object[]) viewerArgs);

        Applet applet = findRSApplet(loader);

        return new OSRSContainer(applet);
    }
}

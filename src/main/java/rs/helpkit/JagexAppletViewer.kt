package rs.helpkit

import rs.helpkit.pref.RSPreferences
import rs.helpkit.reflect.Classes
import rs.helpkit.util.io.Internet
import java.applet.Applet
import java.io.File
import java.io.IOException
import java.lang.invoke.MethodHandles
import java.lang.reflect.Field
import java.net.JarURLConnection
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object JagexAppletViewer {

    private const val VIEWER_URL = "http://oldschool.runescape.com/downloads/jagexappletviewer.jar"
    private const val CONFIG = "http://oldschool%s.runescape.com/l=en/jav_config.ws"

    @Throws(ClassNotFoundException::class)
    private// Applet is deprecated in Java9
    fun findAppletField(loader: ClassLoader): Field {
        val viewer = loader.loadClass("app.appletviewer") ?: throw IllegalStateException("app.appletviewer not found")
        return Classes.findField(viewer) { field -> field.type == Applet::class.java }
                ?: throw IllegalStateException("Applet not found")
    }

    private fun locateApplet(field: Field): Applet {
        for (i in 1..10) {
            val applet = field.get(null)
            if (applet != null) {
                return applet as Applet
            }
        }

        error("Unable to locate RS applet")
    }

    @Throws(ClassNotFoundException::class, IllegalAccessException::class, InterruptedException::class)
    private fun findRSApplet(loader: ClassLoader): Applet {
        val appletField = findAppletField(loader)

        return locateApplet(appletField)
//        try {
//        } catch (e: IllegalStateException) {
//            System.err.println(e.message)
//            System.exit(1)
//        }
    }

    @Throws(IOException::class)
    fun download(targetFile: String) {
        var outdated = true

        val jar = File(targetFile)

        if (jar.exists()) {
            val localHash = JarFile(jar).manifest.hashCode()
            val jarManifestURL = "jar:$VIEWER_URL!/META-INF/MANIFEST.MF"
            val remoteViewer = URL(jarManifestURL).openConnection() as JarURLConnection
            val remoteHash = remoteViewer.manifest.hashCode()
            if (localHash == remoteHash) { // The local and remote manifest hashcode match, already up-to-date
                outdated = false
            }
        }

        if (outdated) {
            // TODO: show download progress of jagexappletviewer.jar
            Internet.download(VIEWER_URL, jar.absolutePath)
        } else {
            // TODO: show that jagexappletviewer.jar already up-to-date
        }
    }

    @Throws(Throwable::class)
    // Applet is deprecated in Java9
    fun run(targetFile: String): OSRSContainer {
        val jar = File(targetFile)
        val loader = URLClassLoader.newInstance(arrayOf(jar.toURI().toURL()))

        val main = loader.loadClass("jagexappletviewer")
        val exec = main.getDeclaredMethod("main", Array<String>::class.java)

        System.setProperty("com.jagex.config", String.format(CONFIG, RSPreferences.defaultWorld))
        System.setProperty("sun.java2d.nodraw", "true")

        val mainHandle = MethodHandles.lookup().unreflect(exec)
        val viewerArgs = arrayOf("./")
        mainHandle.invoke(*viewerArgs as Array<Any>)

        val applet = findRSApplet(loader)

        return OSRSContainer(applet)
    }
}

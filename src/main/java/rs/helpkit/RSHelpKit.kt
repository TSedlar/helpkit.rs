package rs.helpkit

import rs.helpkit.pref.HKConfig

import java.io.File

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object RSHelpKit {

    @Throws(Throwable::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val viewerJar = HKConfig.path(HKConfig.JAGEX, "jagexappletviewer.jar")
        try {
            JagexAppletViewer.download(viewerJar)
        } catch (e: Exception) {
            println("Failed to download new jagexappletviewer.jar..")
        }

        if (File(viewerJar).exists()) {
            JagexAppletViewer.run(viewerJar)
        }
    }
}

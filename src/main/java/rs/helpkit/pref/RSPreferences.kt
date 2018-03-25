package rs.helpkit.pref

import rs.helpkit.api.game.access.Camera
import java.awt.Dimension
import java.awt.Point
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object RSPreferences {

    private val PROPS = Properties()

    val defaultSize: Dimension
        get() {
            val width = Integer.parseInt(PROPS[Keys.DEFAULT_WIDTH].toString())
            val height = Integer.parseInt(PROPS[Keys.DEFAULT_HEIGHT].toString())
            return Dimension(width, height)
        }

    val defaultLocation: Point
        get() {
            val x = Integer.parseInt(PROPS[Keys.DEFAULT_X].toString())
            val y = Integer.parseInt(PROPS[Keys.DEFAULT_Y].toString())
            return Point(x, y)
        }

    var defaultWorld: Int
        get() = Integer.parseInt(PROPS[Keys.DEFAULT_WORLD].toString())
        set(world) {
            PROPS.setProperty(Keys.DEFAULT_WORLD, Integer.toString(world))
        }

    var defaultZoomLevel: Int
        get() = Integer.parseInt(PROPS[Keys.DEFAULT_ZOOM_LEVEL].toString())
        set(zoom) {
            PROPS.setProperty(Keys.DEFAULT_ZOOM_LEVEL, Integer.toString(zoom))
        }

    private interface Keys {
        companion object {
            const val DEFAULT_WORLD = "default_world"
            const val DEFAULT_WIDTH = "default_width"
            const val DEFAULT_HEIGHT = "default_height"
            const val DEFAULT_X = "default_x"
            const val DEFAULT_Y = "default_y"
            const val DEFAULT_ZOOM_LEVEL = "default_zoom_level"
        }
    }

    init {
        val propertyFile = File(HKConfig.HOME, "prefs.txt")
        if (propertyFile.exists()) {
            try {
                FileInputStream(propertyFile).use { `in` -> PROPS.load(`in`) }
            } catch (e: IOException) {
                System.err.println("Failed to load prefs.txt")
            }

        } else {
        }
        setIfInvalid(Keys.DEFAULT_WORLD, "70")
        setIfInvalid(Keys.DEFAULT_WIDTH, "765")
        setIfInvalid(Keys.DEFAULT_HEIGHT, "503")
        setIfInvalid(Keys.DEFAULT_X, "-1")
        setIfInvalid(Keys.DEFAULT_Y, "-1")
        setIfInvalid(Keys.DEFAULT_ZOOM_LEVEL, Camera.ZOOM_OUT_MAX.toString())
        Runtime.getRuntime().addShutdownHook(Thread {
            try {
                FileOutputStream(propertyFile).use { out -> PROPS.store(out, "RSHelpKit preferences") }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        })
    }

    private fun setIfInvalid(key: String, value: String) {
        if (PROPS.getProperty(key) == null) {
            PROPS.setProperty(key, value)
        }
    }

    fun setDefaultSize(width: Int, height: Int) {
        PROPS.setProperty(Keys.DEFAULT_WIDTH, Integer.toString(width))
        PROPS.setProperty(Keys.DEFAULT_HEIGHT, Integer.toString(height))
    }

    fun setDefaultLocation(x: Int, y: Int) {
        PROPS.setProperty(Keys.DEFAULT_X, Integer.toString(x))
        PROPS.setProperty(Keys.DEFAULT_Y, Integer.toString(y))
    }
}

package rs.helpkit.pref

import rs.helpkit.api.game.access.Camera
import rs.helpkit.api.game.access.Skills
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
    private val XP_PROPS = Properties()

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

    private fun loadProps(file: File, props: Properties) {
        if (file.exists()) {
            try {
                FileInputStream(file).use { props.load(it) }
            } catch (e: IOException) {
                System.err.println("Failed to load prefs.txt")
            }
        }
    }

    init {
        val mainProps = File(HKConfig.HOME, "prefs.txt")
        val xpProps = File(HKConfig.HOME, "xp_prefs.txt")

        loadProps(mainProps, PROPS)
        loadProps(xpProps, XP_PROPS)

        // set default PROPS
        setIfInvalid(PROPS, Keys.DEFAULT_WORLD, "70")
        setIfInvalid(PROPS, Keys.DEFAULT_WIDTH, "765")
        setIfInvalid(PROPS, Keys.DEFAULT_HEIGHT, "503")
        setIfInvalid(PROPS, Keys.DEFAULT_X, "-1")
        setIfInvalid(PROPS, Keys.DEFAULT_Y, "-1")
        setIfInvalid(PROPS, Keys.DEFAULT_ZOOM_LEVEL, Camera.ZOOM_OUT_MAX.toString())

        // set default XP_PROPS
        Skills.values().forEach {
            val name = it.skillName().toLowerCase()
            setIfInvalid(XP_PROPS, "${name}_enabled", "false")
            setIfInvalid(XP_PROPS, "${name}_frame_xpos", "-1")
            setIfInvalid(XP_PROPS, "${name}_frame_ypos", "-1")
            setIfInvalid(XP_PROPS, "${name}_frame_popped", "false")
        }

        Runtime.getRuntime().addShutdownHook(Thread {
            try {
                FileOutputStream(mainProps).use { out -> PROPS.store(out, "RSHelpKit preferences") }
                FileOutputStream(xpProps).use { out -> XP_PROPS.store(out, "XP plugin preferences") }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        })
    }

    private fun setIfInvalid(props: Properties, key: String, value: String) {
        if (props.getProperty(key) == null) {
            props.setProperty(key, value)
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

    fun isSkillEnabled(skill: Skills): Boolean {
        val name = skill.skillName().toLowerCase()
        return XP_PROPS.getProperty("${name}_enabled")!!.toBoolean()
    }

    fun setSkillEnabled(skill: Skills, enabled: Boolean) {
        val name = skill.skillName().toLowerCase()
        XP_PROPS.setProperty("${name}_enabled", enabled.toString())
    }

    fun skillLocationFor(skill: Skills): Point {
        val name = skill.skillName().toLowerCase()
        val x = XP_PROPS.getProperty("${name}_frame_xpos").toInt()
        val y = XP_PROPS.getProperty("${name}_frame_ypos").toInt()
        return Point(x, y)
    }

    fun setSkillLocation(skill: Skills, x: Int, y: Int) {
        val name = skill.skillName().toLowerCase()
        XP_PROPS.setProperty("${name}_frame_xpos", x.toString())
        XP_PROPS.setProperty("${name}_frame_ypos", y.toString())
    }

    fun isSkillPopout(skill: Skills): Boolean {
        val name = skill.skillName().toLowerCase()
        return XP_PROPS.getProperty("${name}_frame_popped")!!.toBoolean()
    }

    fun setSkillPopped(skill: Skills, popped: Boolean) {
        val name = skill.skillName().toLowerCase()
        XP_PROPS.setProperty("${name}_frame_popped", popped.toString())
    }
}

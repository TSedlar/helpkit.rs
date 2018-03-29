package rs.helpkit.api.game.access

import rs.helpkit.api.game.data.Sprites
import rs.helpkit.api.raw.Fields
import rs.helpkit.pref.RSPreferences
import java.awt.Rectangle
import java.awt.event.MouseEvent
import java.awt.geom.Area
import kotlin.math.max
import kotlin.math.min

/**
 * @author Tyler Sedlar
 * @since 3/23/2018
 */
object Camera {

    const val ZOOM_IN_MIN = 715 // these seem reversed, but the higher number = more zoomed in
    const val ZOOM_OUT_MAX = 100
    const val RS_DEFAULT_ZOOM = 175

    private var level: Int = RS_DEFAULT_ZOOM

    init {
        level = RSPreferences.defaultZoomLevel
    }

    fun setZoom() {
        Fields.setShort("Client#cameraZoom", level.toShort(), null)
//        Fields.setShort("Client#cameraZoomIFace", this.level.toShort(), null)
    }

    fun zoomIn(increment: Int) {
        level = min(ZOOM_IN_MIN, level + increment)
        setZoom()
    }

    fun zoomOut(increment: Int) {
        level = max(ZOOM_OUT_MAX, level - increment)
        setZoom()
    }

    fun zoomArea(): Area {
        val clickable = Area()
        val vw = Fields.asInt("Client#viewportWidth")
        val vh = Fields.asInt("Client#viewportHeight")
        val xs = Interfaces.positionsX()
        val ys = Interfaces.positionsY()
        val ws = Interfaces.widths()
        val hs = Interfaces.heights()

        clickable.add(Area(Rectangle(0, 0, vw, vh)))

        for (i in 0 until xs!!.size) {
            if (hs!![i] != vh) {
                clickable.subtract(Area(Rectangle(xs[i], ys!![i], ws!![i], hs[i])))
            }
        }

        Interfaces.findChild {
            !it.hidden() && (it.spriteId() == Sprites.INTERFACE_EXIT1 ||
                    it.spriteId() == Sprites.INTERFACE_EXIT1_HOVER ||
                    it.spriteId() == Sprites.INTERFACE_EXIT2 ||
                    it.spriteId() == Sprites.INTERFACE_EXIT2_HOVER ||
                    it.spriteId() == Sprites.INTERFACE_EXIT3 ||
                    it.spriteId() == Sprites.INTERFACE_EXIT3_HOVER)
        }?.let {
            val parent = it.parent()
            if (parent != null) {
                clickable.subtract(Area(parent.bounds()))
            }
        }

        return clickable
    }

    fun inZoomArea(e: MouseEvent): Boolean = zoomArea().contains(e.point)
}
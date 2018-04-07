package rs.helpkit.api.game.access

import rs.helpkit.api.game.wrapper.model.Hull
import rs.helpkit.api.raw.Fields
import rs.helpkit.api.raw.Methods
import java.awt.Point

/**
 * @author Tyler Sedlar
 * @since 4/7/2018
 */
object Projection {

    private val CAM_LOCK = Object()

    fun toScreen(tileX: Int, tileY: Int, height: Int): Point? {
        return synchronized(CAM_LOCK, {
            Methods.invoke("Client#toScreen", null, tileX, tileY, height)
            val toScreenX = Fields.asInt("Client#toScreenX")
            val toScreenY = Fields.asInt("Client#toScreenY")
            return@synchronized Point(toScreenX, toScreenY)
        })
    }

    fun hull(x: Int, y: Int, z: Int, left: Int, right: Int, bottom: Int, top: Int, levitation: Int,
             height: Int): Hull {
        var px = x
        var py = y
        val hull = Hull()
        px += 64
        py += 64
        val t1 = toScreen(px - left, py - top, z + levitation)
        val t2 = toScreen(px + right, py - top, z + levitation)
        val t3 = toScreen(px + right, py + bottom, z + levitation)
        val t4 = toScreen(px - left, py + bottom, z + levitation)
        val b1 = toScreen(px - left, py - top, z + levitation + height)
        val b2 = toScreen(px + right, py - top, z + levitation + height)
        val b3 = toScreen(px + right, py + bottom, z + levitation + height)
        val b4 = toScreen(px - left, py + bottom, z + levitation + height)
        if (t1 != null && t2 != null && t3 != null && t4 != null && b1 != null && b2 != null && b3 != null && b4 != null) {
            val points = arrayOf(t1, t2, t3, t4, t1, b1, b2, b3, b4, b1, b2, t2, b2, b3, t3, b3, b4, t4)
            for (p in points) {
                if (onScreen(p.x, p.y)) {
                    hull.addPoint(p.x, p.y)
                }
            }
        }
        return hull
    }

    fun viewportWidth(): Int = Fields.asInt("Client#viewportWidth")

    fun viewportHeight(): Int = Fields.asInt("Client#viewportHeight")

    fun onScreen(x: Int, y: Int): Boolean {
        return if (Client.resizable()) {
            x >= 0 && y >= 0 && x <= viewportWidth() && y <= viewportHeight()
        } else {
            x in 2..515 && y in 2..336
        }
    }

}
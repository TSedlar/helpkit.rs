package rs.helpkit.api.rsui

import java.awt.Graphics2D
import java.awt.Rectangle

/**
 * @author Tyler Sedlar
 * @since 3/21/2018
 */
abstract class FXChildComponent : FXComponent() {

    var parent: RSWindow? = null

    abstract fun render(g: Graphics2D, rx: Int, ry: Int)

    override fun render(g: Graphics2D) {
        if (parent != null && visible) {
            render(g, parent!!.x + parent!!.xOff + xOff, parent!!.y + parent!!.yOff + yOff)
        }
    }

    override fun exactBounds(): Rectangle {
        return if (parent == null) {
            Rectangle(x, y, w, h)
        } else {
            Rectangle(parent!!.x + x + xOff, parent!!.y + y + yOff, w, h)
        }
    }
}
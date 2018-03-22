package rs.helpkit.api.rsui

import java.awt.Graphics2D

abstract class FXChildComponent : FXComponent() {

    var parent: RSWindow? = null

    abstract fun render(g: Graphics2D, rx: Int, ry: Int)

    override fun render(g: Graphics2D) {
        if (parent != null && visible) {
            render(g, parent!!.x, parent!!.y)
        }
    }
}
package rs.helpkit.api.rsui

import rs.helpkit.util.fx.GraphicsState
import java.awt.Graphics2D

/**
 * @author Tyler Sedlar
 * @since 3/21/2018
 */
abstract class RSWindow(w: Int, h: Int) : FXComponent() {

    val children: MutableList<FXComponent> = ArrayList()

    init {
        this.w = w
        this.h = h
    }

    open fun add(child: FXComponent) {
        child.parent = this
        children.add(child)
    }

    override fun render(g: Graphics2D) {
        if (visible) {
            val state = GraphicsState(g)
            render(g, x + xOff, y + yOff)
            state.restore()
            children.forEach {
                if (it is RSContainer) {
                    it.x = x
                    it.y = y
                    it.xOff = xOff
                    it.yOff = yOff
                }
                it.render(g)
            }
        }
    }
}
package rs.helpkit.api.rsui

import java.awt.Graphics2D

/**
 * @author Tyler Sedlar
 * @since 3/21/2018
 */
abstract class RSWindow(w: Int, h: Int) : FXComponent() {

    protected val children: MutableList<FXComponent> = ArrayList()

    init {
        this.w = w
        this.h = h
        onDrag({ _, _, absX, absY ->
            x = absX - (w / 2)
            y = absY - (h / 2)
        })
    }

    open fun add(child: FXComponent) {
        if (child is FXChildComponent) {
            child.parent = this
        }
        children.add(child)
    }

    abstract fun render(g: Graphics2D, rx: Int, ry: Int)

    override fun render(g: Graphics2D) {
        if (visible) {
            render(g, x + xOff, y + yOff)
            children
                    .filter { child -> child is FXChildComponent }
                    .map { child -> child as FXChildComponent }
                    .forEach { child -> child.render(g, x + xOff, y + yOff) }
        }
    }
}
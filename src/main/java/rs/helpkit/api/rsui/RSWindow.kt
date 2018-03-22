package rs.helpkit.api.rsui

import java.awt.Graphics2D

abstract class RSWindow(w: Int, h: Int) : FXComponent() {

    protected val children: MutableList<FXChildComponent> = ArrayList()

    init {
        this.w = w
        this.h = h
    }

    fun add(child: FXChildComponent) {
        child.parent = this
        children.add(child)
    }

    abstract fun render(g: Graphics2D, rx: Int, ry: Int)

    override fun render(g: Graphics2D) {
        if (visible) {
            render(g, x, y)
            children.forEach { child -> child.render(g, x, y) }
        }
    }
}
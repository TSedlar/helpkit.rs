package rs.helpkit.api.rsui

import rs.helpkit.api.util.Renderable
import rs.helpkit.util.fx.GraphicsState
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author Tyler Sedlar
 * @since 3/21/2018
 */
abstract class FXComponent : Renderable {

    var parent: RSWindow? = null

    companion object {
        val VISIBLE_COMPONENTS: MutableList<FXComponent> = CopyOnWriteArrayList()
    }

    var x: Int = 0
    var y: Int = 0
    var w: Int = 0
    var h: Int = 0
    var xOff: Int = 0
    var yOff: Int = 0
    internal var visible: Boolean = false
        set(visible) {
            field = visible
            if (visible) {
                VISIBLE_COMPONENTS.add(this)
            } else {
                VISIBLE_COMPONENTS.remove(this)
            }
        }

    val mouseListeners: MutableList<MouseListener> = ArrayList()
    val mouseMotionListeners: MutableList<MouseMotionListener> = ArrayList()

    internal val states: MutableList<(self: FXComponent) -> Unit> = ArrayList()

    init {
        visible = true
    }

    var width: Int
        get() = this.w
        set(w) {
            this.w = w
        }
    var height: Int
        get() = this.h
        set(h) {
            this.h = h
        }

    fun drawn(): Boolean {
        if (!visible) {
            return false
        }
        return if (this.parent != null) {
            var parent = this.parent
            while (parent != null) {
                if (!parent.visible) {
                    return false
                }
                if (parent.parent != null) {
                    parent = parent.parent
                } else {
                    break
                }
            }
            parent!!.visible
        } else {
            visible
        }
    }

    fun hide() {
        visible = false
    }

    fun show() {
        visible = true
    }

    open fun bounds(): Rectangle {
        return Rectangle(x + xOff, y + yOff, w, h)
    }

    open fun exactBounds(): Rectangle {
        return if (parent == null) {
            Rectangle(x, y, w, h)
        } else {
            Rectangle(parent!!.x + x + xOff, parent!!.y + y + yOff, w, h)
        }
    }

    fun processStates() {
        states.forEach { it(this) }
    }

    abstract fun render(g: Graphics2D, rx: Int, ry: Int)

    override fun render(g: Graphics2D) {
        if (parent != null && visible) {
            val state = GraphicsState(g)
            render(g, parent!!.x + parent!!.xOff + xOff, parent!!.y + parent!!.yOff + yOff)
            state.restore()
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : FXComponent> T.bindState(state: (self: T) -> Unit): T {
    states.add(state as (self: FXComponent) -> Unit)
    return this
}

fun <T : FXComponent> T.onClick(callback: (x: Int, y: Int) -> Unit): T {
    mouseListeners.add(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            if (visible) {
                callback(e.x, e.y)
            }
        }
    })
    return this
}

fun <T : FXComponent> T.onClickRelease(callback: (x: Int, y: Int) -> Unit): T {
    mouseListeners.add(object : MouseAdapter() {
        override fun mouseReleased(e: MouseEvent) {
            if (visible) {
                callback(e.x, e.y)
            }
        }
    })
    return this
}

fun <T : FXComponent> T.onHover(onHover: (x: Int, y: Int) -> Unit, onExit: (() -> Unit)? = null): T {
    mouseMotionListeners.add(object : MouseAdapter() {
        override fun mouseMoved(e: MouseEvent) {
            if (visible) {
                onHover(e.x, e.y)
            }
        }
    })
    onExit?.let {
        mouseListeners.add(object : MouseAdapter() {
            override fun mouseExited(e: MouseEvent) {
                if (visible) {
                    onExit()
                }
            }
        })
    }
    return this
}

fun <T : FXComponent> T.onDrag(callback: (x: Int, y: Int, absX: Int, absY: Int) -> Unit): T {
    mouseMotionListeners.add(object : MouseAdapter() {
        override fun mouseDragged(e: MouseEvent) {
            if (visible) {
                callback(e.x, e.y, e.xOnScreen, e.yOnScreen)
            }
        }
    })
    return this
}

fun <T : FXComponent> T.onDrag(callback: (x: Int, y: Int) -> Unit): T {
    mouseMotionListeners.add(object : MouseAdapter() {
        override fun mouseDragged(e: MouseEvent) {
            if (visible) {
                callback(e.x, e.y)
            }
        }
    })
    return this
}
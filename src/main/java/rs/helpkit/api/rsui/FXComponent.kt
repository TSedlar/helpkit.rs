package rs.helpkit.api.rsui

import rs.helpkit.api.util.Renderable
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

    companion object {
        val VISIBLE_COMPONENTS: MutableList<FXComponent> = CopyOnWriteArrayList()
    }

    var x: Int = 0
    var y: Int = 0
    var w: Int = 0
    var h: Int = 0
    var xOff: Int = 0
    var yOff: Int = 0
    var visible: Boolean = false
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

    fun bounds(): Rectangle {
        return Rectangle(x + xOff, y + yOff, w, h)
    }

    open fun exactBounds(): Rectangle = bounds()

    fun onClick(callback: (x: Int, y: Int) -> Unit): FXComponent {
        mouseListeners.add(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (visible) {
                    callback(e.x, e.y)
                }
            }
        })
        return this
    }

    fun onHover(onHover: (x: Int, y: Int) -> Unit, onExit: (() -> Unit)? = null): FXComponent {
        mouseMotionListeners.add(object : MouseAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                if (visible) {
                    onHover(e.x, e.y)
                }
            }
        })
        onExit?.let {
            mouseListeners.add(object: MouseAdapter() {
                override fun mouseExited(e: MouseEvent) {
                    if (visible) {
                        onExit()
                    }
                }
            })
        }
        return this
    }

    fun onDrag(callback: (x: Int, y: Int, absX: Int, absY: Int) -> Unit): FXComponent {
        mouseMotionListeners.add(object : MouseAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                if (visible) {
                    callback(e.x, e.y, e.xOnScreen, e.yOnScreen)
                }
            }
        })
        return this
    }

    fun onDrag(callback: (x: Int, y: Int) -> Unit): FXComponent {
        mouseMotionListeners.add(object : MouseAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                if (visible) {
                    callback(e.x, e.y)
                }
            }
        })
        return this
    }
}
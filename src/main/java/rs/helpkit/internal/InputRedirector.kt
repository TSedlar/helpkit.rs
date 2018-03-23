package rs.helpkit.internal

import rs.helpkit.api.game.GameMenu
import rs.helpkit.api.rsui.FXComponent
import java.awt.event.*
import javax.swing.event.MouseInputAdapter

/**
 * @author Tyler Sedlar
 * @since 3/21/2018
 */
object InputRedirector {

    @JvmStatic
    fun createMouseAdapter(initialMouseListeners: Array<MouseListener>,
                           initialMouseMotionListeners: Array<MouseMotionListener>,
                           initialMouseWheelListeners: Array<MouseWheelListener>): MouseInputAdapter {
        return object : MouseInputAdapter() {
            override fun mouseEntered(e: MouseEvent) {
                if (!execFXMouseEvents(e)) {
                    initialMouseListeners.forEach { ml -> ml.mouseEntered(e) }
                }
            }

            override fun mouseExited(e: MouseEvent) {
                if (!execFXMouseEvents(e)) {
                    initialMouseListeners.forEach { ml -> ml.mouseExited(e) }
                }
            }

            override fun mouseClicked(e: MouseEvent) {
                if (!execFXMouseEvents(e)) {
                    initialMouseListeners.forEach { ml -> ml.mouseClicked(e) }
                }
            }

            override fun mousePressed(e: MouseEvent) {
                if (!execFXMouseEvents(e)) {
                    initialMouseListeners.forEach { ml -> ml.mousePressed(e) }
                }
            }

            override fun mouseReleased(e: MouseEvent) {
                if (!execFXMouseEvents(e)) {
                    initialMouseListeners.forEach { ml -> ml.mouseReleased(e) }
                }
            }

            override fun mouseMoved(e: MouseEvent) {
                if (!execFXMouseEvents(e)) {
                    initialMouseMotionListeners.forEach { mml -> mml.mouseMoved(e) }
                }
            }

            override fun mouseDragged(e: MouseEvent) {
                if (!execFXMouseEvents(e)) {
                    initialMouseMotionListeners.forEach { mml -> mml.mouseDragged(e) }
                }
            }

            override fun mouseWheelMoved(e: MouseWheelEvent) {
                if (!execFXMouseEvents(e)) {
                    initialMouseWheelListeners.forEach { mml -> mml.mouseWheelMoved(e) }
                }
            }
        }
    }

    fun execFXMouseEvents(e: MouseEvent): Boolean {
        var block = false
        FXComponent.VISIBLE_COMPONENTS.forEach { component ->
            val bounds = component.exactBounds()
            if (bounds.contains(e.point)) {
                val tx = e.x - bounds.x
                val ty = e.y - bounds.y
                val translated = MouseEvent(e.component, e.id, e.`when`, e.modifiersEx, tx, ty,
                        e.x, e.y, e.clickCount, e.isPopupTrigger, e.button)
                component.mouseListeners.forEach { ml ->
                    when {
                        e.id == MouseEvent.MOUSE_CLICKED -> ml.mouseClicked(translated)
                        e.id == MouseEvent.MOUSE_PRESSED -> ml.mousePressed(translated)
                        e.id == MouseEvent.MOUSE_RELEASED -> ml.mouseReleased(translated)
                    }
                }
                component.mouseMotionListeners.forEach { ml ->
                    when {
                        e.id == MouseEvent.MOUSE_MOVED -> ml.mouseMoved(translated)
                        e.id == MouseEvent.MOUSE_DRAGGED -> ml.mouseDragged(translated)
                    }
                }
                block = true
            }
        }
        if (e.id == MouseEvent.MOUSE_PRESSED) {
            GameMenu.CUSTOM_MENU_ADAPTERS.values.forEach {
                if (GameMenu.visible()) {
                    it.mousePressed(e)
                }
            }
        }
        return block && !GameMenu.visible()
    }
}
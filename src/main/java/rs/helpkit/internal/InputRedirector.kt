package rs.helpkit.internal

import rs.helpkit.OSRSContainer
import rs.helpkit.api.game.access.Camera
import rs.helpkit.api.game.access.GameMenu
import rs.helpkit.api.rsui.FXComponent
import rs.helpkit.api.util.Time
import java.awt.event.*
import javax.swing.SwingUtilities
import javax.swing.event.MouseInputAdapter

/**
 * @author Tyler Sedlar
 * @since 3/21/2018
 */
object InputRedirector {

    @JvmStatic
    fun createMouseAdapter(container: OSRSContainer, initialMouseListeners: Array<MouseListener>,
                           initialMouseMotionListeners: Array<MouseMotionListener>,
                           initialMouseWheelListeners: Array<MouseWheelListener>): MouseInputAdapter {
        return object : MouseInputAdapter() {
            override fun mouseEntered(e: MouseEvent) {
                if (!execFXMouseEvents(container, e)) {
                    initialMouseListeners.forEach { ml -> ml.mouseEntered(e) }
                }
            }

            override fun mouseExited(e: MouseEvent) {
                if (!execFXMouseEvents(container, e)) {
                    initialMouseListeners.forEach { ml -> ml.mouseExited(e) }
                }
            }

            override fun mouseClicked(e: MouseEvent) {
                if (!execFXMouseEvents(container, e)) {
                    initialMouseListeners.forEach { ml -> ml.mouseClicked(e) }
                }
            }

            override fun mousePressed(e: MouseEvent) {
                if (!execFXMouseEvents(container, e)) {
                    initialMouseListeners.forEach { ml -> ml.mousePressed(e) }
                }
            }

            override fun mouseReleased(e: MouseEvent) {
                if (!execFXMouseEvents(container, e)) {
                    initialMouseListeners.forEach { ml -> ml.mouseReleased(e) }
                }
            }

            override fun mouseMoved(e: MouseEvent) {
                if (!execFXMouseEvents(container, e)) {
                    initialMouseMotionListeners.forEach { mml -> mml.mouseMoved(e) }
                }
            }

            override fun mouseDragged(e: MouseEvent) {
                if (!execFXMouseEvents(container, e)) {
                    initialMouseMotionListeners.forEach { mml -> mml.mouseDragged(e) }
                }
            }

            override fun mouseWheelMoved(e: MouseWheelEvent) {
                if (Camera.inZoomArea(e)) {
                    if (e.unitsToScroll < 0) {
                        Camera.zoomIn(Math.abs(e.unitsToScroll * e.scrollAmount))
                    } else {
                        Camera.zoomOut(Math.abs(e.unitsToScroll * e.scrollAmount))
                    }
                } else {
                    initialMouseWheelListeners.forEach { mml -> mml.mouseWheelMoved(e) }
                }
            }
        }
    }

    private var lastMenuOpen = -1L

    fun execFXMouseEvents(container: OSRSContainer, e: MouseEvent): Boolean {
        var block = false
        when {
            e.id == MouseEvent.MOUSE_CLICKED -> {
                container.plugins.forEach { it.mouseClicked(e) }
            }
            e.id == MouseEvent.MOUSE_PRESSED -> {
                container.plugins.forEach { it.mousePressed(e) }
                if (e.button == MouseEvent.BUTTON1) {
                    GameMenu.VALID_CUSTOM_MENU_ITEMS.values.forEach {
                        if (GameMenu.visible()) {
                            if (it.bounds?.contains(e.point)!!) {
                                block = false
                                it.handler()
                            }
                        }
                    }
                }
            }
            e.id == MouseEvent.MOUSE_RELEASED -> {
                container.plugins.forEach { it.mouseReleased(e) }
            }
            e.id == MouseEvent.MOUSE_MOVED -> {
                container.plugins.forEach { it.mouseMoved(e) }
            }
            e.id == MouseEvent.MOUSE_DRAGGED -> {
                container.plugins.forEach { it.mouseDragged(e) }
            }
        }
        if (GameMenu.visible()) {
            lastMenuOpen = Time.now()
            return false
        } else if (lastMenuOpen != -1L && Time.now() - lastMenuOpen < 100) {
            return false
        }
        FXComponent.VISIBLE_WINDOWS.forEach { component ->
            var usable = true
            if (!component.drawn() || (component.parent != null && !component.parent!!.drawn())) {
                usable = false
            }
            if (usable) {
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
                            e.id == MouseEvent.MOUSE_DRAGGED -> {
                                if (SwingUtilities.isLeftMouseButton(e)) {
                                    ml.mouseDragged(translated)
                                }
                            }
                        }
                    }
                    block = true
                } else {
                    component.mouseListeners.forEach { it.mouseExited(e) }
                }
            }
        }
        return block
    }
}
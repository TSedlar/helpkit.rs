package rs.helpkit.internal

import rs.helpkit.OSRSContainer
import rs.helpkit.util.fx.GraphicsState
import java.awt.Canvas
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.event.MouseWheelListener
import java.awt.image.BufferedImage
import java.util.*
import javax.swing.event.MouseInputAdapter

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
class RSCanvas(container: OSRSContainer, var original: Canvas) : Canvas() {

    var buffer: BufferedImage? = null
    var raw: BufferedImage? = null

    private var normalized = false
    private var hidden = false

    private val initialMouseListeners: Array<MouseListener>
    private val initialMouseMotionListeners: Array<MouseMotionListener>
    private val initialMouseWheelListeners: Array<MouseWheelListener>
    var mouseInputAdapter: MouseInputAdapter? = null

    val consumers: MutableList<(Graphics2D) -> Unit> = ArrayList()

    init {
        bounds = original.bounds
        raw = BufferedImage(GAME_SIZE.width, GAME_SIZE.height, BufferedImage.TYPE_3BYTE_BGR)
        buffer = BufferedImage(GAME_SIZE.width, GAME_SIZE.height, BufferedImage.TYPE_3BYTE_BGR)
        initialMouseListeners = original.mouseListeners
        initialMouseMotionListeners = original.mouseMotionListeners
        initialMouseWheelListeners = original.mouseWheelListeners
        mouseInputAdapter = InputRedirector.createMouseAdapter(container, initialMouseListeners,
                initialMouseMotionListeners, initialMouseWheelListeners)
    }

    fun normalize() {
        normalized = true
    }

    fun hideGraphics() {
        hidden = true
    }

    override fun hasFocus(): Boolean {
        return true
    }

    override fun getGraphics(): Graphics? {
        val g = original.graphics
        if (hidden) {
            g!!.color = background
            g.fillRect(0, 0, width, height)
            return g
        }
        if (normalized) {
            return g
        }
        if (g != null && buffer != null && raw != null) {
            val paint = buffer!!.createGraphics()
            paint.drawImage(raw, 0, 0, null)
            consumers.forEach { consumer ->
                try {
                    val state = GraphicsState(paint)
                    consumer(paint)
                    state.restore()
                } catch (ignored: Exception) {
                    ignored.printStackTrace()
                }
            }
            paint.dispose()
            g.drawImage(buffer, 0, 0, null)
            g.dispose()
            return raw!!.createGraphics()
        }
        return null
    }

    override fun hashCode(): Int {
        return original.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return original == other
    }

    companion object {

        val GAME_SIZE = Dimension(765, 503)
    }
}

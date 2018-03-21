package rs.helpkit.internal

import rs.helpkit.api.raw.Fields
import rs.helpkit.api.util.Time
import rs.helpkit.util.fx.GraphicsState
import java.applet.Applet
import java.awt.Canvas
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.util.*

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
class RSCanvas(private var original: Canvas?) : Canvas() {

    var buffer: BufferedImage? = null
    var raw: BufferedImage? = null

    private var normalized = false
    private var hidden = false
    private var running = false

    val consumers: MutableList<(Graphics2D) -> Unit> = ArrayList()

    init {
        bounds = original?.bounds
        raw = BufferedImage(GAME_SIZE.width, GAME_SIZE.height, BufferedImage.TYPE_3BYTE_BGR)
        buffer = BufferedImage(GAME_SIZE.width, GAME_SIZE.height, BufferedImage.TYPE_3BYTE_BGR)
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
        val g = original!!.graphics
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
        return original!!.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return original == other
    }

    @Suppress("DEPRECATION") // Applet is deprecated in Java9
    fun startReplacementTask(applet: Applet) {
        if (!running) {
            running = true
            Thread {
                while (running) {
                    try {
                        original = applet.getComponent(0) as Canvas
                        bounds = original!!.bounds
                        val producer = Fields["Client#interfaceProducer"]
                        Fields.set("ComponentProducer#component", this, producer)
                        Fields.set("GameEngine#canvas", this, applet)
                        Time.sleep(1000)
                    } catch (e: Exception) {
                    }

                }
            }.start()
        }
    }

    fun stopReplacementTask() {
        running = false
    }

    companion object {

        val GAME_SIZE = Dimension(765, 503)
    }
}

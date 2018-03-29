package rs.helpkit.internal

import rs.helpkit.OSRSContainer
import rs.helpkit.api.game.access.Client
import rs.helpkit.api.raw.Fields
import rs.helpkit.api.util.Time
import rs.helpkit.reflect.ObjectProxy
import java.applet.Applet

/**
 * @author Tyler Sedlar
 * @since 3/24/2018
 */
object ReplacementTasks {

    object Canvas {

        @Suppress("DEPRECATION") // Applet is deprecated in Java9
        fun startReplacementTask(canvas: RSCanvas, applet: Applet) {
            Thread {
                while (true) {
                    try {
                        canvas.original = applet.getComponent(0) as java.awt.Canvas
                        canvas.bounds = canvas.original.bounds
                        canvas.original.mouseListeners.forEach { canvas.original.removeMouseListener(it) }
                        canvas.original.mouseMotionListeners.forEach { canvas.original.removeMouseMotionListener(it) }
                        canvas.original.mouseWheelListeners.forEach { canvas.original.removeMouseWheelListener(it) }
                        canvas.original.keyListeners.forEach { canvas.original.removeKeyListener(it) }
                        canvas.original.addMouseListener(canvas.mouseInputAdapter)
                        canvas.original.addMouseMotionListener(canvas.mouseInputAdapter)
                        canvas.original.addMouseWheelListener(canvas.mouseInputAdapter)
                        canvas.original.addKeyListener(canvas.keyInputAdapter)
                        val producer = Fields["Client#interfaceProducer"]
                        Fields.set("ComponentProducer#component", canvas, producer)
                        Fields.set("GameEngine#canvas", canvas, applet)
                        Time.sleep(1000)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }.start()
        }
    }

    object Streams {

        fun startReplacementTask(container: OSRSContainer) {
            Thread({
                while (true) {
                    try {
                        Fields.set("Client#useBufferedSocket", false)
                        val context = Client.packetContext()
                        if (context.validate()) {
                            val socket = context.socket()
                            if (socket.validate()) {
                                val input = socket.input()
                                if (input != null) {
                                    if (input !is RSInputStream) {
                                        Fields.set("RSSocket#input", RSInputStream(container, input), socket.get())
                                    }
                                }
                                val output = socket.output()
                                if (output != null) {
                                    if (output !is RSOutputStream) {
                                        Fields.set("RSSocket#output", RSOutputStream(container, output), socket.get())
                                    }
                                }
                            }
                        }
                        Time.sleep(1000)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }).start()
        }
    }

    object Proxies {

        fun startPacketReplacement() {
            Thread({
                var set = false
                while (!set) {
                    val ctx = Client.packetContext()
                    if (ctx.validate()) {
                        val buffer = ctx.buffer()
                        if (buffer != null) {
                            val field = HookLoader.DIRECT_FIELDS["PacketContext#buffer"]!!
                            set = ObjectProxy.override(field, arrayOf(Int::class.java), arrayOf(5000), ctx.get(),
                                    { method, args ->

                                    })
                            println("override PacketContext#buffer @ $buffer")
                        }
                    }
                    Time.sleep(50)
                }
            }).start()
        }

        fun startCSEventQueueReplacement(container: OSRSContainer) {
            Thread({
                var set = false
                while (!set) {
                    val queue = Fields["Client#csPriority0Queue"]
                    if (queue != null) {
                        set = CSEventQueue.proxy(container, queue)
                    }
                    Time.sleep(50)
                }
            }).start()
        }
    }
}
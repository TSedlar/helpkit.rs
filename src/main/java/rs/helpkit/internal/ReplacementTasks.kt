package rs.helpkit.internal

import rs.helpkit.api.game.Client
import rs.helpkit.api.raw.Fields
import rs.helpkit.api.util.Time
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
                        canvas.original.addMouseListener(canvas.mouseInputAdapter)
                        canvas.original.addMouseMotionListener(canvas.mouseInputAdapter)
                        canvas.original.addMouseWheelListener(canvas.mouseInputAdapter)
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

        fun startReplacementTask() {
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
                                        Fields.set("RSSocket#input", RSInputStream(context, input), socket.get())
                                    }
                                }
                                val output = socket.output()
                                if (output != null) {
                                    if (output !is RSOutputStream) {
                                        Fields.set("RSSocket#output", RSOutputStream(context, output), socket.get())
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
}
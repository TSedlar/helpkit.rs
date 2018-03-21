package rs.helpkit

import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.util.Renderable
import rs.helpkit.api.util.Time
import rs.helpkit.internal.HookLoader
import rs.helpkit.internal.RSCanvas
import rs.helpkit.plugins.Example
import rs.helpkit.pref.RSPreferences
import java.applet.Applet
import java.awt.*
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.image.BufferedImage
import java.util.*

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
class OSRSContainer(applet: Applet) {

    private val loader: ClassLoader
    private var canvas: Canvas
    private var customCanvas: RSCanvas
    private var window: Window
    private var panel: Panel? = null

    val plugins: MutableList<Plugin> = ArrayList()

    init {
        println("Game has loaded")
        loader = applet.javaClass.classLoader
        canvas = applet.getComponent(0) as Canvas
        window = findWindow(canvas)
        customCanvas = RSCanvas(canvas)
        hideAllButCanvas(applet)
        HookLoader.load(loader)
        plugins.add(Example())
        plugins.forEach { plugin ->
            val manifest = plugin.javaClass.getAnnotation(Manifest::class.java)
            if (manifest.loop) {
                Thread(plugin).start()
            }
        }
        customCanvas.consumers.add({ g ->
            plugins.stream()
                    .filter { p -> p.enabled && p.validate() && p is Renderable }
                    .forEach { p -> (p as Renderable).render(g) }
        })
    }

    private fun findWindow(canvas: Canvas?): Window {
        var component: Component? = canvas
        do {
            component = component?.parent ?: break
            if (component is Window) {
                return component
            }
        } while (component != null)
        error("Unable to find window")
    }

    @Suppress("DEPRECATION") // Applet is deprecated in Java9
    private fun hideAllButCanvas(applet: Applet) {
        canvas = applet.getComponent(0) as Canvas
        window = findWindow(canvas)
        val components = window.components
        for (child in components) {
            if (child is Container) {
                panel = child as Panel
                for (gc in child.components) {
                    if (gc !is Panel) {
                        gc.isVisible = false
                        child.remove(gc)
                    }
                }
                break
            }
        }
        var i = 0
        while (i < 10 && canvas.mouseListeners.isEmpty()) {
            Time.sleep(1000)
            i++
        }
        customCanvas = RSCanvas(canvas)
        panel!!.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                val size = panel!!.size
                RSPreferences.setDefaultSize(size.width, size.height)
                canvas.preferredSize = size
                customCanvas.preferredSize = size
                customCanvas.raw = BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR)
                customCanvas.buffer = BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR)
            }
        })
        window.addComponentListener(object : ComponentAdapter() {
            override fun componentMoved(e: ComponentEvent?) {
                RSPreferences.setDefaultLocation(window.x, window.y)
            }
        })
        customCanvas.startReplacementTask(applet)
        window.revalidate()
        val size = RSPreferences.defaultSize
        canvas.preferredSize = size
        customCanvas.preferredSize = size
        customCanvas.raw = BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR)
        customCanvas.buffer = BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR)
        panel!!.size = size
        panel!!.preferredSize = size
        window.pack()
        val location = RSPreferences.defaultLocation
        if (location.x == -1 && location.y == -1) {
            window.setLocationRelativeTo(null)
        } else {
            window.location = location
        }
        window.toFront()
        window.requestFocus()
    }
}

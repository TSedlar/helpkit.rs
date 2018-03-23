package rs.helpkit

import com.google.common.eventbus.EventBus
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.slf4j.LoggerFactory
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.GameMenu
import rs.helpkit.api.manifest
import rs.helpkit.api.util.Renderable
import rs.helpkit.api.util.Time
import rs.helpkit.dev.services.HookReloaderService
import rs.helpkit.internal.HookLoader
import rs.helpkit.internal.RSCanvas
import rs.helpkit.internal.event.EventChecker
import rs.helpkit.internal.event.GEOfferEventChecker
import rs.helpkit.internal.event.VarpbitEventChecker
import rs.helpkit.pref.HKConfig
import rs.helpkit.pref.RSPreferences
import rs.helpkit.util.io.Resources
import java.applet.Applet
import java.awt.*
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.geom.Area
import java.awt.image.BufferedImage

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
class OSRSContainer(applet: Applet) {

    private val logger = LoggerFactory.getLogger(OSRSContainer::class.java)

    private val loader: ClassLoader
    private var canvas: Canvas
    private var customCanvas: RSCanvas
    private var window: Window
    private var panel: Panel? = null
    private val hookReloader = HookReloaderService(HKConfig.path("data")) {
        loadHooks()
    }

    val plugins = loadPlugins()
    private val checkers: MutableList<EventChecker> = arrayListOf()

    private fun loadHooks() {
        println("Loading hooks")
        HookLoader.load(loader)
    }

    private fun loadPlugins(): List<Plugin> {
        val reflections = Reflections("rs.helpkit", SubTypesScanner())
        val pluginClasses = reflections.getSubTypesOf(Plugin::class.java)
        return pluginClasses.mapNotNull { clazz ->
            try {
                val plugin = clazz.newInstance()
                if (plugin.manifest() == null) {
                    logger.info("${clazz.name} is of type Plugin, but does not have a Manifest")
                    return@mapNotNull null
                }
                return@mapNotNull plugin
            } catch (e: IllegalAccessException) {
            } catch (e: InstantiationException) {
            }

            null
        }
    }

    init {
        println("Game has loaded")
        loader = applet.javaClass.classLoader
        canvas = applet.getComponent(0) as Canvas
        window = findWindow(canvas)
        customCanvas = RSCanvas(canvas)
        hideAllButCanvas(applet)
        loadHooks()
        hookReloader.start()

        val bus = EventBus()
        checkers.add(VarpbitEventChecker(bus))
        checkers.add(GEOfferEventChecker(bus))
        checkers.forEach { it.start() }

        logger.info("Starting looping plugins")
        plugins.forEach { plugin ->
            plugin.manifest()?.run {
                if (loop) {
                    logger.info("Starting $name v$version")
                    plugin.start()
                    logger.debug("Started $name v$version")
                }
            }
        }
        logger.info("Started looping plugins")

        plugins.forEach { bus.register(it) }

        logger.info("Installing fonts")
        Resources.installFonts()

        customCanvas.consumers.add({ g ->
            if (GameMenu.visible()) {
                val bounds = GameMenu.bounds()
                val outside = Area(customCanvas.bounds)
                outside.subtract(Area(bounds))
                g.clip(outside)
            }
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
                canvas.bounds = Rectangle(0, 0, size.width, size.height)
                customCanvas.bounds = Rectangle(0, 0, size.width, size.height)
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

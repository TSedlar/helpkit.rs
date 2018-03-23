package rs.helpkit.plugins

import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.GameMenu
import rs.helpkit.api.game.GameTab
import rs.helpkit.api.game.Interfaces
import rs.helpkit.api.game.wrapper.CustomMenuItem
import rs.helpkit.api.game.wrapper.RTComponent
import rs.helpkit.api.raw.Fields
import rs.helpkit.api.rsui.RSTabContentPanel
import rs.helpkit.api.rsui.RSWindow
import rs.helpkit.api.util.Renderable
import rs.helpkit.api.util.Schedule
import rs.helpkit.util.io.Resources
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage

@Manifest(
        author = "Static",
        name = "PluginTab",
        description = "The plugin that manages every custom plugin that uses the plugin tab",
        version = 1.0
)
class PluginTab : Plugin(), Renderable {

    var tabImage: BufferedImage = Resources.img("/images/ui/tab.png")
    var tabSelectedImage: BufferedImage = Resources.img("/images/ui/tab-selected.png")

    var bounds: Rectangle? = null
    var contentBounds: Rectangle? = null

    var viewing: Boolean = false

    var customWindow: RSWindow? = null
    var customMenuBounds: MutableList<Rectangle> = ArrayList()

    var expTracker = CustomMenuItem("XP Tracker", {
        customWindow = RSTabContentPanel()
    })

    override fun validate(): Boolean {
        return true
    }

    fun findTabContentPanel() : RTComponent? {
        return Interfaces.findChild { child ->
            val text = child.text()
            return@findChild text != null && text.contains("Advanced options")
        }?.parent()?.parent()?.parent()?.parent()
    }

    init {
        GameMenu.CUSTOM_MENU_ADAPTERS.put("CUSTOM_TAB_CLOSE", object: MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                if (GameMenu.visible()) {
                    val actions = GameMenu.actions()
                    if (actions != null && actions[GameMenu.itemCount - 1] == "Options") {
                        if (GameMenu.boundsAt(0).contains(e.point)) {
                            customWindow = null
                        }
                    }
                }
            }
        })
    }

    @Schedule(100)
    fun updateCustomMenuItems() {
        GameMenu.addMenuItems({ actions, _ ->
            actions[GameMenu.itemCount - 1] == "Options"
        }, 1, expTracker)
    }

    @Schedule(100)
    fun updateGeometry() {
        val options = GameTab.OPTIONS.component()
        bounds = options?.bounds()
        viewing = GameTab.current() == GameTab.OPTIONS
        val contents = findTabContentPanel()
        if (contents != null) {
            val arrIdx = contents.arrayIndex()
            if (arrIdx != -1) {
                Fields.set("RTComponent#hidden", customWindow != null, contents.get())
            }
        }
        contentBounds = contents?.bounds()
        if (customWindow != null && !viewing) {
            // force tab to options
            options?.toggleClickEvent()
        }
    }

    override fun render(g: Graphics2D) {
        g.color = Color.GREEN
        if (bounds != null && customWindow != null) {
            if (viewing) {
                g.drawImage(tabSelectedImage, bounds!!.x, bounds!!.y, null)
            } else {
                g.drawImage(tabImage, bounds!!.x, bounds!!.y, null)
            }
            if (customWindow != null && contentBounds != null) {
                customWindow!!.x = contentBounds!!.x
                customWindow!!.y = contentBounds!!.y
                customWindow!!.width = contentBounds!!.width
                customWindow!!.height = contentBounds!!.height
                customWindow!!.render(g)
            }
        }
        g.color = Color.GREEN
        customMenuBounds.forEach { bounds -> g.draw(bounds) }
    }
}
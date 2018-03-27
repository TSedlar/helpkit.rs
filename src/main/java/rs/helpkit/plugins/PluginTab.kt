package rs.helpkit.plugins

import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.access.GameMenu
import rs.helpkit.api.game.access.GameTab
import rs.helpkit.api.game.access.Interfaces
import rs.helpkit.api.game.wrapper.RTComponent
import rs.helpkit.api.raw.Fields
import rs.helpkit.api.rsui.RSContainer
import rs.helpkit.api.util.Renderable
import rs.helpkit.api.util.Schedule
import rs.helpkit.plugins.tab.*
import rs.helpkit.util.io.Resources
import java.awt.Graphics2D
import java.awt.Rectangle
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

    var customTab: CustomTab? = null
    var customPanel: RSContainer? = null

    val tabs = listOf(
            GrandExchangeTab(this),
            HiscoresTab(this),
            NotesTab(this),
            XPTrackerTab(this),
            TimersTab(this)
    )

    override fun validate(): Boolean {
        return true
    }

    fun findTabContentPanel(): RTComponent? {
        return Interfaces.findChild { child ->
            val text = child.text()
            return@findChild text != null && text.contains("Advanced options")
        }?.parent()?.parent()?.parent()?.parent()
    }

    override fun mousePressed(e: MouseEvent) {
        if (GameMenu.visible() && e.button == MouseEvent.BUTTON1) {
            val actions = GameMenu.actions()
            if (actions != null && actions[GameMenu.itemCount - 1] == "Options") {
                if (GameMenu.boundsAt(0).contains(e.point)) {
                    if (customTab != null) {
                        customPanel!!.hide()
                    }
                    customTab = null
                    customPanel = null
                    GameTab.OPTIONS.component()?.toggleClickEvent()
                    Fields.set("GameMenu#visible", false, null)
                }
            }
        }
    }

    @Schedule(100)
    fun processTabs() {
        CustomTab.process(tabs)
    }

    @Schedule(100)
    fun updateGeometry() {
        val options = GameTab.OPTIONS.component()
        bounds = options?.bounds()
        viewing = GameTab.current() == GameTab.OPTIONS
        if (customPanel != null && !viewing) {
            customPanel!!.hide()
        }
        val contents = findTabContentPanel()
        if (contents != null) {
            val arrIdx = contents.arrayIndex()
            if (arrIdx != -1) {
                Fields.set("RTComponent#hidden", customPanel != null, contents.get())
            }
        }
        contentBounds = contents?.bounds()
    }

    override fun render(g: Graphics2D) {
        tabs.forEach { it.render(g) }
        if (bounds != null && customTab != null) {
            customPanel?.let {
                if (viewing) {
                    it.show()
                } else {
                    it.hide()
                }
            }
            if (viewing) {
                g.drawImage(tabSelectedImage, bounds!!.x, bounds!!.y, null)
            } else {
                g.drawImage(tabImage, bounds!!.x, bounds!!.y, null)
            }
            val tabCenterX = bounds!!.x + (bounds!!.width / 2) - (customTab!!.icon.width / 2)
            val tabCenterY = bounds!!.y + (bounds!!.height / 2) - (customTab!!.icon.height / 2)
            g.drawImage(customTab!!.icon, tabCenterX, tabCenterY, null)
            customPanel?.let {
                if (contentBounds != null) {
                    it.x = contentBounds!!.x
                    it.y = contentBounds!!.y
                    it.width = contentBounds!!.width
                    it.height = contentBounds!!.height
                }
                it.render(g)
            }
        }
        contentBounds?.let { testAtRuntime(g, it) }
    }

    private fun testAtRuntime(g: Graphics2D, bounds: Rectangle) {
//        val x = bounds.x + 59
//        val y = bounds.y + 22
    }
}
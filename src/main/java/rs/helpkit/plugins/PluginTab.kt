package rs.helpkit.plugins

import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.GameTab
import rs.helpkit.api.util.Renderable
import rs.helpkit.api.util.Schedule
import rs.helpkit.util.io.Resources
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle
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
    var viewing: Boolean = false

    override fun validate(): Boolean {
        return true
    }

    @Schedule(100)
    fun testInterfaces() {
        bounds = GameTab.OPTIONS.component()?.bounds()
        viewing = GameTab.current() == GameTab.OPTIONS
    }

    override fun render(g: Graphics2D) {
        if (bounds != null) {
            g.color = Color.GREEN
//            g.draw(bounds)
            if (viewing) {
                g.drawImage(tabSelectedImage, bounds!!.x, bounds!!.y, null)
            } else {
                g.drawImage(tabImage, bounds!!.x, bounds!!.y, null)
            }
        }
    }
}
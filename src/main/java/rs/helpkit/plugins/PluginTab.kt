package rs.helpkit.plugins

import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.Client
import rs.helpkit.api.game.Interfaces
import rs.helpkit.api.util.Renderable
import rs.helpkit.api.util.Schedule
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle

@Manifest(
        author = "Static",
        name = "PluginTab",
        description = "The plugin that manages every custom plugin that uses the plugin tab",
        version = 1.0
)
class PluginTab : Plugin(), Renderable {

    val PARENT_FIXED = 548
    val PARENT_RESIZABLE = 161
    val PARENT_RESIZABLE_STONES = 164

    var bounds: Rectangle = Rectangle(0, 0, 0, 0)

    override fun validate(): Boolean {
        return true
    }

    @Schedule(5000)
    fun testInterfaces() {
        val match = Interfaces.findChild { child ->
            if (child.hidden() || -Math.abs(child.cycle()) + Math.abs(Client.cycle()) > 5) {
                return@findChild false
            }
            val actions = child.actions()
            if (actions != null && actions.isNotEmpty()) {
                return@findChild actions[0] == "Options"
            }
            return@findChild false
        }
        if (match != null) {
            bounds = match.bounds()
        }
    }

    override fun render(g: Graphics2D) {
        g.color = Color.GREEN
        g.draw(bounds)
    }
}
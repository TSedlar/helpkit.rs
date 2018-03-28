package rs.helpkit.plugins

import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.access.GameMenu
import rs.helpkit.api.util.Renderable
import java.awt.Graphics2D

@Manifest(author = "Static", name = "Action Swapper", description = "Rearranges menu items", version = 1.0)
class ActionSwapper : Plugin(), Renderable {

    override fun validate(): Boolean {
        return true
    }

    override fun onAwtCycle() {
        GameMenu.sort("Exchange", "Talk-to")
    }

    override fun render(g: Graphics2D) {

    }
}
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
        GameMenu.sort(
                "Break", "Toggle Destination", "Eat", "Drink", "Wear", "Wield", "Rub", "Lay", "Set-up",
                "Fill", "Empty", "Check", "Deposit", "Settings", "Teleport", "Features", "Disassemble",
                "Gem Mine", "Commune", "Use", "Bury", "Pickpocket", "Attack", "Exchange", "Talk-to"
        )
    }

    override fun render(g: Graphics2D) {

    }
}
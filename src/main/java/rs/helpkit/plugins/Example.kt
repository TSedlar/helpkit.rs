package rs.helpkit.plugins

import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.Players
import rs.helpkit.api.util.Renderable
import java.awt.Color
import java.awt.Graphics2D

@Manifest(author = "Static", name = "Example Plugin", description = "Just an example", version = 1.0)
class Example : Plugin(), Renderable {

    override fun validate(): Boolean {
        return true
    }

    override fun loop(): Int {
//        println("hi")
        return 100
    }

    override fun render(g: Graphics2D) {
        g.color = Color.GREEN
        g.drawString("data: " + Players.local()?.level(), 100, 100)
    }
}

package rs.helpkit.plugins

import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.Client
import rs.helpkit.api.game.Players
import rs.helpkit.api.util.Renderable
import java.awt.Color
import java.awt.Graphics2D

@Manifest(author = "Static", name = "Example Plugin", description = "Just an example", version = 1.0)
class Example : Plugin(), Renderable {

    var varpCache: IntArray? = null

    override fun validate(): Boolean {
        return true
    }

    override fun loop(): Int {
        val varps = Client.varps()
        if (varps != null && varpCache != null) {
            (0 until varps.size)
                    .filter { varpCache!![it] != varps[it] }
                    .forEach { println("Varp change @ " + it + ": " + varpCache!![it] + " -> " + varps[it]) }
        }
        varpCache = varps?.clone()
        return 100
    }

    override fun render(g: Graphics2D) {
        g.color = Color.GREEN
        g.drawString("data: " + Players.local()?.name()?.text(), 100, 100)
    }
}

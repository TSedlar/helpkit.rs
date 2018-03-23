package rs.helpkit.dev.plugins

import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.Client
import rs.helpkit.api.game.Interfaces
import rs.helpkit.api.util.Renderable
import rs.helpkit.api.util.Schedule
import java.awt.*

/**
 * @since 03/22/2018
 */
@Manifest(author = "Kyle", name = "Kappa", description = "kappa", version = 1.0, loop = true, enabled = false)
class InterfaceDebugPlugin : Plugin(), Renderable {
    private var candidates = emptyList<Rectangle>()

    @Schedule(50)
    fun update() {
        val mouse = Client.mouse()
        val xCandidates = Interfaces.positionsX() ?: IntArray(0)
        val yCandidates = Interfaces.positionsY() ?: IntArray(0)
        val heights = Interfaces.heights() ?: IntArray(0)
        val widths = Interfaces.widths() ?: IntArray(0)

        val locations = arrayListOf<Rectangle>()
        for (index in 0 until xCandidates.size) {
            val nw = Point(xCandidates[index], yCandidates[index])
            locations.add(Rectangle(nw, Dimension(widths[index], heights[index])))
        }

        candidates = locations.filter { it.contains(mouse) }.filter { it.x != 0 && it.y != 0 }
    }

    override fun render(g: Graphics2D) {
//        g.drawString(Client.mouse().toString(), 50, 50)
        candidates.forEach {
            g.color = Color(255, 175, 175, 30)
            g.fill(it)
            g.color = Color(255, 175, 175, 110)
            g.draw(it)
        }
//        g.drawString(Interfaces.positionsX()?.zip(Interfaces.positionsY() ?: IntArray(0))?.toList()?.toString(), 50, 68)
//        g.drawString(Interfaces.positionsX()?.toList().toString(), 50, 68)
        g.drawString(candidates.toList().toString(), 50, 86)
    }

    override fun validate(): Boolean {
        return Client.mouse().x != -1
    }
}
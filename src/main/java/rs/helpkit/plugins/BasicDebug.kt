package rs.helpkit.plugins

import com.google.common.eventbus.Subscribe
import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.access.*
import rs.helpkit.api.game.color.Colors
import rs.helpkit.api.game.listener.event.GEOfferUpdated
import rs.helpkit.api.game.listener.event.VarpChanged
import rs.helpkit.api.raw.Fields
import rs.helpkit.api.util.Renderable
import rs.helpkit.util.io.Resources
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.util.concurrent.atomic.AtomicInteger

@Manifest(author = "Static", name = "Basic Debug", description = "Shows info used for creating plugins", version = 1.0)
class BasicDebug : Plugin(), Renderable {

    val DEBUG_FONT = Font(Resources.FONT_RS_BOLD_2, Font.PLAIN, 16)
    val RS_WHITE = Color(204, 204, 204)

    override fun validate(): Boolean = true

    @Subscribe
    fun onGrandExchangeOfferUpdated(event: GEOfferUpdated) {
        val offer = GrandExchange[event.slot]
        println("[GE] #${event.slot} => $offer")
    }

    @Subscribe
    fun onVarbitChanged(event: VarpChanged) {
        println("Varbit@${event.index} = ${Varpbits.get()[event.index]}")
    }

    override fun render(g: Graphics2D) {
        g.font = DEBUG_FONT
        g.color = RS_WHITE
        val y = AtomicInteger(if (hasOpt()) 32 else 17)

        Players.local().let {
            drawTestString(g, "${it.worldX}, ${it.worldY}", y)
        }
        ItemContainers.INVENTORY?.iterate { item ->
            item.name?.let {
                if (it.startsWith("Clue scroll")) {
                    val type = it.split("(")[1].split(")")[0]
                    drawTestString(g, "Clue(${item.id} = $type)", y)
                }
            }
        }
    }

    private fun hasOpt(): Boolean {
        val startX = 5
        val startY = 9
        for (i in startX..(startX + 5)) {
            val pixel = Colors.pixelAt(i, startY)
            if (pixel.r >= 180 && pixel.r == pixel.g && pixel.r == pixel.b) {
                return true
            }
        }
        return false
    }

    private fun drawTestString(g: Graphics2D, str: String, y: AtomicInteger) {
        g.drawString(str, 3, y.getAndAdd(15))
    }
}

package rs.helpkit.plugins

import com.google.common.eventbus.Subscribe
import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.access.Client
import rs.helpkit.api.game.access.GrandExchange
import rs.helpkit.api.game.access.Players
import rs.helpkit.api.game.access.Varpbits
import rs.helpkit.api.game.listener.event.GEOfferUpdated
import rs.helpkit.api.game.listener.event.VarpChanged
import rs.helpkit.api.rsui.RSFrame
import rs.helpkit.api.rsui.RSImage
import rs.helpkit.api.rsui.RSLabel
import rs.helpkit.api.rsui.onClick
import rs.helpkit.api.util.Renderable
import rs.helpkit.api.util.Schedule
import rs.helpkit.api.util.Time
import rs.helpkit.util.io.Resources
import java.awt.Color
import java.awt.Graphics2D

@Manifest(author = "Static", name = "Example Plugin", description = "Just an example", version = 1.0)
class Example : Plugin(), Renderable {

    var data: Any? = null

    override fun validate(): Boolean {
        return true
    }

    @Subscribe
    fun onGrandExchangeOfferUpdated(event: GEOfferUpdated) {
        val offer = GrandExchange[event.slot]
        println("[GE] #${event.slot} => $offer")
    }

    @Subscribe
    fun onVarbitChanged(event: VarpChanged) {
        println("Varbit@${event.index} = ${Varpbits.get()[event.index]}")
    }

    @Schedule(100)
    fun updateData() {
        data = Players.local()?.name()?.text()
    }

    val texture = Resources.img("/images/textures/btn-texture.png")

    override fun render(g: Graphics2D) {
        g.color = Color.GREEN
//        g.drawString("data: $data", 100, 100)
    }
}

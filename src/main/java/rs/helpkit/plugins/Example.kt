package rs.helpkit.plugins

import com.google.common.eventbus.Subscribe
import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.GrandExchange
import rs.helpkit.api.game.Players
import rs.helpkit.api.game.access.Varpbits
import rs.helpkit.api.game.listener.event.GEOfferUpdated
import rs.helpkit.api.game.listener.event.VarpChanged
import rs.helpkit.api.rsui.RSFrame
import rs.helpkit.api.rsui.RSImage
import rs.helpkit.api.rsui.RSLabel
import rs.helpkit.api.util.Renderable
import rs.helpkit.api.util.Schedule
import rs.helpkit.util.io.Resources
import java.awt.Color
import java.awt.Graphics2D

@Manifest(author = "Static", name = "Example Plugin", description = "Just an example", version = 1.0)
class Example : Plugin(), Renderable {

    var data: Any? = null
    var frame: RSFrame = RSFrame(164, 49)

    var counter: Int = 0

    init {
        frame.add(RSImage("/images/ui/close-red.png", frame.w - 22, 2))
        frame.add(RSLabel(0, 0)
                .useFont(Resources.FONT_RS_SMALL, 16)
                .bindTo { (counter++).toString() })
    }

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

    @Schedule(1000)
    fun updateData() {
        data = Players.local()?.name()?.text()
    }

    override fun render(g: Graphics2D) {
        g.color = Color.GREEN
        g.drawString("data: " + frame.width, 100, 100)
        frame.visible = false
        frame.render(g)
    }
}

package rs.helpkit.plugins

import com.google.common.eventbus.Subscribe
import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.access.*
import rs.helpkit.api.game.data.Varps
import rs.helpkit.api.game.listener.event.GEOfferUpdated
import rs.helpkit.api.game.listener.event.VarpChanged
import rs.helpkit.api.util.Renderable
import rs.helpkit.api.util.Schedule
import java.awt.Color
import java.awt.Graphics2D
import java.util.*

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
//        data = Varpbits.get()[361]
//        data = Players.local()?.name()?.text()
//        data = Fields["TestSuite#hook"]
    }

    override fun render(g: Graphics2D) {
        g.color = Color.GREEN
//        var y = 100
//        ItemContainers.INVENTORY?.iterate { item ->
//            g.drawString("${item.idx}: id=${item.id}, stack=${item.stack}, name=${item.name}", 100, y)
//            y += 15
//        }
//        g.drawString("data: ${Varps.MISC_FAVOR}, ${Varps.MISC_COFFER}", 100, 100)
    }
}

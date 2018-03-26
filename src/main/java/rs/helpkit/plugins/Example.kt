package rs.helpkit.plugins

import com.google.common.eventbus.Subscribe
import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.access.GrandExchange
import rs.helpkit.api.game.access.Players
import rs.helpkit.api.game.access.Skills
import rs.helpkit.api.game.access.Varpbits
import rs.helpkit.api.game.listener.event.GEOfferUpdated
import rs.helpkit.api.game.listener.event.VarpChanged
import rs.helpkit.api.rsui.RSFrame
import rs.helpkit.api.rsui.RSImage
import rs.helpkit.api.rsui.RSLabel
import rs.helpkit.api.util.Renderable
import rs.helpkit.api.util.Schedule
import rs.helpkit.util.io.Resources
import java.awt.*
import java.awt.geom.Area

@Manifest(author = "Static", name = "Example Plugin", description = "Just an example", version = 1.0)
class Example : Plugin(), Renderable {

    var data: Any? = null
    var frame: RSFrame = RSFrame(164, 49)

    var counter: Int = 0

    init {
        frame.x = 150
        frame.y = 150
        frame.add(RSImage("/images/ui/close-red.png", null, frame.w - 22, 2)
                .onClick({ x, y ->
                    frame.visible = false
                    println("$x, $y")
                }))
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

    @Schedule(100)
    fun updateData() {
        data = Players.local()?.name()?.text()
    }

    val texture = Resources.img("/images/textures/btn-texture.png")

    override fun render(g: Graphics2D) {
//        val img = Skills.ICONS[Skills.ATTACK.index()]
//        g.drawImage(img, 100, 100, null)
//        g.color = Color.GREEN
//        g.drawString("data: $data", 100, 100)
//        frame.visible = false
//        frame.render(g)
    }
}

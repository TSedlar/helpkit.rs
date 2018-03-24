package rs.helpkit.plugins

import com.google.common.eventbus.Subscribe
import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.Client
import rs.helpkit.api.game.GrandExchange
import rs.helpkit.api.game.Interfaces
import rs.helpkit.api.game.Players
import rs.helpkit.api.game.access.Varpbits
import rs.helpkit.api.game.listener.event.GEOfferUpdated
import rs.helpkit.api.game.listener.event.VarpChanged
import rs.helpkit.api.raw.Fields
import rs.helpkit.api.raw.Methods
import rs.helpkit.api.rsui.FXComponent
import rs.helpkit.api.rsui.RSFrame
import rs.helpkit.api.rsui.RSImage
import rs.helpkit.api.rsui.RSLabel
import rs.helpkit.api.util.Renderable
import rs.helpkit.api.util.Schedule
import rs.helpkit.util.Serializer
import rs.helpkit.util.io.Resources
import java.awt.Color
import java.awt.Graphics2D

@Manifest(author = "Static", name = "Example Plugin", description = "Just an example", version = 1.0)
class Example : Plugin(), Renderable {

    var data: Any? = null
    var frame: RSFrame = RSFrame(164, 49)

    var counter: Int = 0

    init {
        frame.x = 150
        frame.y = 150
        frame.add(RSImage("/images/ui/close-red.png", frame.w - 22, 2)
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
//        Fields.setShort("Client#cameraZoom", 50, null)
        val comp = Interfaces.findById(10551296)
        if (comp != null) {
//            println("test")
//            Fields.set("RTComponent#windowOpenListener", null, comp.get())
//            println(Fields["RTComponent#windowOpenListener", comp.get()])
        }
    }

    override fun render(g: Graphics2D) {
        g.color = Color.GREEN
        val ctx = Client.packetContext().get()
        val buf = Fields["PacketContext#packetBuffer", ctx] // have to clone..
        if (buf != null) {
            val copy = Serializer.cloneObject(buf)
            val op = Methods.invoke("PacketBuffer#readOpcode", copy)
            g.drawString("ctx: $ctx", 100, 100)
            g.drawString("pkt: $buf", 100, 120)
            g.drawString("op: $op", 100, 140)
        }
//        frame.visible = false
        frame.render(g)
//        g.color = Color.GREEN
//        FXComponent.VISIBLE_COMPONENTS.forEach { component ->
//            g.draw(component.exactBounds())
//        }
    }
}

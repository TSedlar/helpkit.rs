package rs.helpkit.api.game.access

import rs.helpkit.api.game.wrapper.PacketContext
import rs.helpkit.api.game.wrapper.PacketNode
import rs.helpkit.api.raw.Fields
import java.awt.Point

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object Client {

    val FRAME_STANDARD = 548
    val FRAME_RESIZABLE = 161
    val FRAME_RESIZABLE_WITH_PANELS = 164

    fun mouse(): Point {
        return Point(Fields.asInt("Client#mouseX"), Fields.asInt("Client#mouseY"))
    }

    fun fps(): Int = Fields.asInt("Client#fps")

    fun world(): Int = Fields.asInt("Client#currentWorld")

    fun selectedSpellName(): String? = Fields.asString("Client#selectedSpellName")

    fun varps(): IntArray? = Fields.asIntArray("Client#varps")

    fun cycle(): Int = Fields.asInt("Client#globalCycle")

    fun hudIndex(): Int = Fields.asInt("Client#hudIndex")

    fun resizable(): Boolean = hudIndex() != FRAME_STANDARD

    fun packetContext(): PacketContext = PacketContext(Fields["Client#packetContext"])

    fun packetNodes(): List<PacketNode> {
        val list: MutableList<PacketNode> = ArrayList()
        Fields.asArray("Client#packetNodes")?.forEach { node ->
            if (node != null) {
                list.add(PacketNode(node))
            }
        }
        return list
    }
}

package rs.helpkit.api.game.access

import rs.helpkit.api.game.wrapper.PacketContext
import rs.helpkit.api.game.wrapper.PacketNode
import rs.helpkit.api.raw.Fields
import java.awt.Color
import java.awt.Point
import java.awt.image.BufferedImage

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object Client {

    val FRAME_STANDARD = 548
    val FRAME_RESIZABLE = 161
    val FRAME_RESIZABLE_WITH_PANELS = 164

    val STATE_CREDENTIALS = 10
    val STATE_PLAYING = 25
    val STATE_IN_GAME = 30

    val COMP_ACCOUNT_INFO = 378

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

    fun levels(): IntArray? = Fields.asIntArray("Client#currentSkillLevels")

    fun baseLevels(): IntArray? = Fields.asIntArray("Client#baseSkillLevels")

    fun experiences(): IntArray? = Fields.asIntArray("Client#skillExperiences")

    fun connectionState(): Int = Fields.asInt("Client#connectionState")

    fun loggedIn(): Boolean {
        val hud = hudIndex()
        if (hudIndex() != -1) {
            Fields["Client#loadedInterfaces"]?.let {
                val array = it as BooleanArray
                return connectionState() >= STATE_PLAYING && array[hud] && !array[COMP_ACCOUNT_INFO]
            }
        }
        return false
    }

    fun rgbAt(x: Int, y: Int): Int {
        Fields["Client#interfaceProducer"]?.let {
            Fields["ComponentProducer#image", it].let {
                val img = it as BufferedImage
                val rgb = img.getRGB(5, 5)
                println("${rgb shr 16 and 255}, ${rgb shr 8 and 255}, ${rgb and 255}")
            }
        }
        return Color.BLACK.rgb
    }

    fun regionBaseX(): Int = Fields.asInt("Client#regionBaseX")

    fun regionBaseY(): Int = Fields.asInt("Client#regionBaseY")

    fun currentPlane(): Int = Fields.asInt("Client#currentPlane")
}

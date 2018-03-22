package rs.helpkit.api.game

import rs.helpkit.api.raw.Fields
import java.awt.Point

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object Client {
    fun mouse(): Point {
        return Point(Fields.asInt("Client#mouseX"), Fields.asInt("Client#mouseY"))
    }

    fun fps(): Int = Fields.asInt("Client#fps")

    fun world(): Int = Fields.asInt("Client#currentWorld")

    fun selectedSpellName(): String? = Fields.asString("Client#selectedSpellName")

    fun varps(): IntArray? = Fields.asIntArray("Client#varps")

    fun cycle(): Int = Fields.asInt("Client#globalCycle")
}

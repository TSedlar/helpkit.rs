package rs.helpkit.api.game

import rs.helpkit.api.raw.Fields

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object Client {

    fun fps(): Int = Fields.asInt("Client#fps")

    fun world(): Int = Fields.asInt("Client#currentWorld")

    fun selectedSpellName(): String? = Fields.asString("Client#selectedSpellName")

    fun varps(): IntArray? = Fields.asIntArray("Client#varps")
}

package rs.helpkit.api.game

import rs.helpkit.api.raw.Fields

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object Client {

    fun fps(): Int {
        return Fields.asInt("Client#fps")
    }

    fun world(): Int {
        return Fields.asInt("Client#currentWorld")
    }

    fun selectedSpellName(): String? {
        return Fields.asString("Client#selectedSpellName")
    }
}

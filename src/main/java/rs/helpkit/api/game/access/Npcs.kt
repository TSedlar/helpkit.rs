package rs.helpkit.api.game.access

import rs.helpkit.api.game.wrapper.locatable.Npc
import rs.helpkit.api.raw.Fields

/**
 * @author Tyler Sedlar
 * @since 4/7/2018
 */
object Npcs {

    fun loaded(): List<Npc>? {
        return Fields.asArray("Client#loadedNpcs")?.filterNotNull()?.map { it -> Npc(it) }
    }
}
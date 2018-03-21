package rs.helpkit.api.game.access

import rs.helpkit.api.raw.Fields

/**
 * @since 03/20/2018
 */
object Varps {
    fun get(): IntArray {
        return Fields.asIntArray("Client#varps") ?: IntArray(0)
    }
}
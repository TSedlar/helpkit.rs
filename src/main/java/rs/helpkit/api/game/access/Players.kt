package rs.helpkit.api.game.access

import rs.helpkit.api.game.wrapper.locatable.Player
import rs.helpkit.api.raw.Fields

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object Players {

    /**
     * Gets the local player instance
     *
     * @return The instance of the local player
     */
    @JvmStatic
    fun local(): Player {
        return Player(Fields["Client#localPlayer"])
    }
}

package rs.helpkit.api.game.wrapper

import rs.helpkit.api.raw.Wrapper

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
class Player(referent: Any?) : Wrapper("Player", referent) {

    /**
     * Gets the combat level of this player
     *
     * @return The combat level of this player
     */
    fun level(): Int {
        return asInt("combatLevel")
    }
}

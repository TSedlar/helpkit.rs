package rs.helpkit.api.game.wrapper.locatable

import rs.helpkit.api.game.wrapper.Nameable

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
class Player(referent: Any?) : RSCharacter("Player", referent) {

    /**
     * Gets the combat level of this player
     *
     * @return The combat level of this player
     */
    fun level(): Int = asInt("combatLevel")

    /**
     * Gets the nameable object of this player
     *
     * @return The nameable object of this player
     */
    fun name(): Nameable? = Nameable(get("name"))
}

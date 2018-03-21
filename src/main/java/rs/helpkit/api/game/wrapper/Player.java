package rs.helpkit.api.game.wrapper;

import rs.helpkit.api.raw.Wrapper;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class Player extends Wrapper {

    public Player(Object referent) {
        super("Player", referent);
    }

    /**
     * Gets the combat level of this player
     *
     * @return The combat level of this player
     */
    public int level() {
        return asInt("combatLevel");
    }
}

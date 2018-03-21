package rs.helpkit.api.game;

import rs.helpkit.api.game.wrapper.Player;
import rs.helpkit.api.raw.Fields;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class Players {

    /**
     * Gets the local player instance
     *
     * @return The instance of the local player
     */
    public static Player local() {
        return new Player(Fields.get("Client#localPlayer"));
    }
}

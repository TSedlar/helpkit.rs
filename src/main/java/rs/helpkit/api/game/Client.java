package rs.helpkit.api.game;

import rs.helpkit.api.raw.Fields;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class Client {

    public static int fps() {
        return Fields.asInt("Client#fps");
    }

    public static int world() {
        return Fields.asInt("Client#currentWorld");
    }

    public static String selectedSpellName() {
        return Fields.asString("Client#selectedSpellName");
    }
}

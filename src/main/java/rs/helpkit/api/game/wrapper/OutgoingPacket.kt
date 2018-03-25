package rs.helpkit.api.game.wrapper

import rs.helpkit.api.raw.Wrapper

/**
 * @author Tyler Sedlar
 * @since 3/24/2018
 */
class OutgoingPacket(referent: Any?) : Wrapper("OutgoingPacket", referent) {

    fun id(): Int = asInt("id")

    fun length(): Int = asInt("length")
}
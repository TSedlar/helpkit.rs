package rs.helpkit.api.game.wrapper

import rs.helpkit.api.raw.Wrapper

/**
 * @author Tyler Sedlar
 * @since 3/24/2018
 */
class PacketNode(referent: Any?) : Wrapper("PacketNode", referent) {

    fun length(): Int = asInt("length")

    fun packet(): OutgoingPacket = OutgoingPacket(get("packet"))

    fun buffer(): Any? = get("buffer")
}
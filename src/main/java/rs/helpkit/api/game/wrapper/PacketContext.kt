package rs.helpkit.api.game.wrapper

import rs.helpkit.api.raw.Wrapper

/**
 * @author Tyler Sedlar
 * @since 3/24/2018
 */
class PacketContext(referent: Any?) : Wrapper("PacketContext", referent) {

    fun socket(): RSSocket = RSSocket(get("socket"))

    fun packet(): IncomingPacket = IncomingPacket(get("packet"))

    fun buffer(): Any? = get("buffer")

    fun packetLength(): Int = asInt("packetLength")
}
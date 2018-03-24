package rs.helpkit.api.game.wrapper

import rs.helpkit.api.raw.Wrapper

class PacketContext(referent: Any?) : Wrapper("PacketContext", referent) {

    fun socket(): RSSocket = RSSocket(get("socket"))

    fun packet(): RSPacket = RSPacket(get("packet"))

    fun buffer(): Any? = get("buffer")
}
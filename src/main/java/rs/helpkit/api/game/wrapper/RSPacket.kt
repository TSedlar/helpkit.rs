package rs.helpkit.api.game.wrapper

import rs.helpkit.api.raw.Wrapper

class RSPacket(referent: Any?) : Wrapper("Packet", referent) {

    fun id(): Int = asInt("id")

    fun length(): Int = asInt("length")
}
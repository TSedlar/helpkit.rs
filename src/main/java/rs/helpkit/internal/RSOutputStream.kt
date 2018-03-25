package rs.helpkit.internal

import rs.helpkit.api.game.access.Client
import rs.helpkit.api.game.wrapper.OutgoingPacket
import rs.helpkit.api.game.wrapper.PacketNode
import rs.helpkit.api.raw.Fields
import java.io.FilterOutputStream
import java.io.OutputStream

/**
 * @author Tyler Sedlar
 * @since 3/24/2018
 */
class RSOutputStream(original: OutputStream) : FilterOutputStream(original) {

    override fun flush() {
        try {
            Client.packetNodes().forEach { node ->
                if (node.validate()) {
                    val packet = node.packet()
                    if (packet.validate()) {
                        onPacketSent(node, packet, packet.id(), packet.length())
                    }
                }
            }
        } catch (e: Exception) {
        }
        super.flush()
    }

    fun onPacketSent(node: PacketNode, packet: OutgoingPacket, id: Int, length: Int) {
        val payload = try {
            Fields["ByteBuffer#buffer", node.buffer()]
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
//        println("outgoing: id=$id, length=$length, payload=$payload")
    }
}
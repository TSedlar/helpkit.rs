package rs.helpkit.internal

import rs.helpkit.api.game.access.Camera
import rs.helpkit.api.game.access.Client
import java.io.FilterInputStream
import java.io.InputStream

/**
 * @author Tyler Sedlar
 * @since 3/24/2018
 */
class RSInputStream(original: InputStream) : FilterInputStream(original) {

    private var prevCycle = 0

    override fun available(): Int {
        try {
            val currCycle = Client.cycle()
            if (prevCycle != currCycle) {
                onCycle()
                prevCycle = currCycle
            }
            onPacketReceived()
        } catch (e: Exception) {
        }
        return super.available()
    }

    fun onCycle() {

    }

    fun onPacketReceived() {
        Camera.setZoom()
        val context = Client.packetContext()
        if (context.validate()) {
            val packet = context.packet()
            if (packet.validate()) {
                val id = packet.id()
                val length = packet.length()
                if (id == 12) {
//                    println("incoming: id=$id, length=$length")
//                    println("  packet: $packet")
//                    val payload = try {
//                        Fields["ByteBuffer#buffer", context.buffer()]
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        null
//                    }
//                    println("  payload: $payload")
                }
            }
        }
    }
}
package rs.helpkit.internal

import rs.helpkit.api.game.Camera
import rs.helpkit.api.game.Client
import rs.helpkit.api.game.wrapper.PacketContext
import java.io.FilterInputStream
import java.io.InputStream
import java.util.*

/**
 * @author Tyler Sedlar
 * @since 3/24/2018
 */
class RSInputStream(var context: PacketContext, original: InputStream) : FilterInputStream(original) {

    private var prevCycle = 0

    override fun available(): Int {
        val currCycle = Client.cycle()
        if (prevCycle != currCycle) {
            onCycle()
            prevCycle = currCycle
        }
        onPacketReceived()
        return super.available()
    }

    override fun read(): Int {
        val read = super.read()
//        println("read(): $read")
        return read
    }

    override fun read(b: ByteArray?): Int {
        val read = super.read(b)
//        println("read(byte[]): $read")
        return read
    }

    override fun read(b: ByteArray?, off: Int, len: Int): Int {
        val read = super.read(b, off, len)
//        println("read(byte[], int, int): off=$off, len=$len, return=$read")
        return read
    }

    fun onCycle() {

    }

    fun onPacketReceived() {
        Camera.setZoom()
    }
}
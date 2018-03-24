package rs.helpkit.internal

import rs.helpkit.api.game.wrapper.PacketContext
import rs.helpkit.api.raw.Fields
import rs.helpkit.api.raw.Methods
import rs.helpkit.util.Serializer
import java.io.FilterOutputStream
import java.io.OutputStream
import java.util.*

/**
 * @author Tyler Sedlar
 * @since 3/24/2018
 */
class RSOutputStream(var context: PacketContext, original: OutputStream): FilterOutputStream(original) {

    val data: MutableList<Int> = ArrayList()

    override fun write(b: Int) {
//        println("write(int): $b")
        data.add(b)
        super.write(b)
    }

    override fun write(b: ByteArray?) {
//        println("write(byte[]): ${Arrays.toString(b)}")
        if (b != null) {
            data.addAll(b.map { it.toInt() })
        }
        super.write(b)
    }

    override fun write(b: ByteArray?, off: Int, len: Int) {
//        println("write(byte[], int, int): off=$off, len=$len, arr=${Arrays.toString(b)}")
        if (b != null) {
            data.addAll(b.map { it.toInt() })
        }
        super.write(b, off, len)
    }

    override fun close() {
        println("closed")
        super.close()
    }

    override fun flush() {
        val buf = Fields["PacketContext#packetBuffer", context.get()]
        if (buf != null) {
            val copy = Serializer.cloneObject(buf)
            val op = Methods.invoke("PacketBuffer#readOpcode", copy)
            if (op == 0) {
                println("idle packet")
            }
            println("sent packet $op")
        }
        data.clear()
        super.flush()
    }
}
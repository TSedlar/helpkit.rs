package rs.helpkit.api.game.access

import rs.helpkit.api.raw.Fields

/**
 * @author Tyler Sedlar
 * @since 03/20/2018
 */
object Varpbits {

    fun get(): IntArray {
        return Fields.asIntArray("Client#varps") ?: IntArray(0)
    }

    operator fun get(idx: Int): Int {
        val all = get()
        return if (idx < all.size) all[idx] else 0
    }
}

/**
 * Gets the range of bits starting from the right side of the bits (regular)
 *
 * Indexing from 0-31
 */
fun Int.rbits(from: Int, to: Int): Int {
    val mask = (0.inv() shl to - from + 1).inv()
    return this shr from and mask
}

/**
 * Gets the range of bits starting from the left side of the bits (no reverse order)
 *
 * Indexing from 0-31
 */
fun Int.lbits(from: Int, to: Int): Int {
    return rbits(31 - to, 31 - from)
}
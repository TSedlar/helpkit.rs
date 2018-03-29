package rs.helpkit.api.game.data

import rs.helpkit.api.game.access.Varpbits
import rs.helpkit.api.game.access.lbits

/**
 * @author Tyler Sedlar
 * @since 3/24/2018
 */
object Varps {

    val RUN_ENERGY // 0/1 = off/on
        get() = Varpbits[173]

    val PRAYER
        get() = Varpbits[83]

    val ACCEPT_AID // 0/1 = off/on
        get() = Varpbits[427]

    val ATTACK_STYLE // 0/1/2/3 (index)
        get() = Varpbits[43]

    val AUTO_RETALIATE // 0/1 = off/on
        get() = Varpbits[172]

    val SPECIAL_ATTACK// 0/1 = off/on
        get() = Varpbits[301]

    val MISC_FAVOR
        get() = ((Varpbits[361].lbits(7, 13) / 127.0) * 100).toInt()

    val MISC_COFFER
        get() = Varpbits[362].lbits(9, 31)
}
package rs.helpkit.internal.event

import com.google.common.eventbus.EventBus
import rs.helpkit.api.game.access.Varps

/**
 * @since 03/20/2018
 */
class VarpEventChecker(eventBus: EventBus) : EventChecker(eventBus) {
    private var last: IntArray = IntArray(0)

    override fun check() {
        val current = Varps.get()
        try {
            val diff = Math.min(last.size, current.size)
//            println("#last=${last.size}, #current=${current.size}")
            for (idx in 0 until diff) {
                if (last[idx] != current[idx]) {
                    println("settings[$idx]: ${last[idx]} => ${current[idx]}")
                }
            }
//            if (diff >= 0) {
//                        .filter { last[it] != current[it] }
//                        .map { VarbitChanged(it) }
//                        .forEach { eventBus.post(it) }
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        last = current
    }
}
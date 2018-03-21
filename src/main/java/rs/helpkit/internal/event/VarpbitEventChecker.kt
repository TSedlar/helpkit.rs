package rs.helpkit.internal.event

import com.google.common.eventbus.EventBus
import rs.helpkit.api.game.Client
import rs.helpkit.api.game.listener.event.VarpbitChanged

/**
 * @since 03/20/2018
 */
class VarpbitEventChecker(eventBus: EventBus) : EventChecker(eventBus) {
    private var cachedVarps: IntArray? = null

    override fun check() {
        val last = cachedVarps ?: IntArray(0)
        val current = Client.varps()
        try {
            if (current == null) {
                return
            }
            val diff = Math.min(last.size, current.size)
            for (idx in 0 until diff) {
                if (last[idx] != current[idx]) {
                    eventBus.post(VarpbitChanged(idx))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        cachedVarps = current?.clone()
    }
}
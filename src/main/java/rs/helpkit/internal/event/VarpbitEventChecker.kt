package rs.helpkit.internal.event

import com.google.common.eventbus.EventBus
import rs.helpkit.api.game.Client
import rs.helpkit.api.game.listener.event.VarpChanged

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
            (0 until diff)
                    .filter { last[it] != current[it] }
                    .forEach { eventBus.post(VarpChanged(it)) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        cachedVarps = current?.clone()
    }
}
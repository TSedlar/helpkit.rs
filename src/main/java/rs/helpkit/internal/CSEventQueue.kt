package rs.helpkit.internal

import rs.helpkit.OSRSContainer
import rs.helpkit.api.game.access.Client
import rs.helpkit.reflect.ObjectProxy
import java.lang.reflect.Field

/**
 * @author Tyler Sedlar
 * @since 3/28/2018
 */
object CSEventQueue {

    val PRIORITY_HIGH = 0
    val PRIORITY_MED = 1
    val PRIORITY_LOW = 2

    fun onScriptEvent(container: OSRSContainer, priority: Int) {
        container.plugins.forEach { it.onAwtCycle() }
    }

    private fun createProxy(container: OSRSContainer, field: Field, priority: Int): Boolean {
        val popFront = HookLoader.DIRECT_METHODS["Deque#popFront"]
        var prevCycle = 0

        return ObjectProxy.override(field, null, { method, _ ->
            if (method.toGenericString() == popFront!!.toGenericString()) {
                val currCycle = Client.cycle()
                if (prevCycle != currCycle) {
                    onScriptEvent(container, priority)
                    prevCycle = currCycle
                }
            }
        })
    }

    fun proxy(container: OSRSContainer, queue: Any): Boolean {
        val field = HookLoader.DIRECT_FIELDS["Client#csPriority0Queue"]!!
        return createProxy(container, field, PRIORITY_HIGH)
    }
}
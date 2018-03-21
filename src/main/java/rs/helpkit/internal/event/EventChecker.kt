package rs.helpkit.internal.event

import com.google.common.eventbus.EventBus
import rs.helpkit.api.util.Time

/**
 * @since 03/20/2018
 */
abstract class EventChecker(
        protected val eventBus: EventBus
) : Thread() {
    abstract fun check()

    override fun run() {
        while (true) {
            check()
            Time.sleep(50)
        }
    }
}
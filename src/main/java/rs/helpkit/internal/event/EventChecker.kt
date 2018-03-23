package rs.helpkit.internal.event

import com.google.common.eventbus.EventBus
import rs.helpkit.api.util.Time

private const val PERIOD_MILLIS = 50L

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
            Time.sleep(PERIOD_MILLIS)
        }
    }
}
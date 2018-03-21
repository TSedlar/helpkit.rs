package rs.helpkit.internal.event

import com.google.common.eventbus.EventBus

/**
 * @since 03/20/2018
 */
abstract class EventChecker(
        protected val eventBus: EventBus
) {
    abstract fun check(): Unit
}
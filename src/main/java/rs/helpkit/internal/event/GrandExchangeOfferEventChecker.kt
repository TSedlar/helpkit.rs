package rs.helpkit.internal.event

import com.google.common.eventbus.EventBus
import rs.helpkit.api.game.GrandExchangeOffer
import rs.helpkit.api.game.GrandExchangeOffers
import rs.helpkit.api.game.PoorlyNamedGrandExchangeOffer
import rs.helpkit.api.game.listener.event.GrandExchangeOfferUpdated

/**
 * @since 03/20/2018
 */
class GrandExchangeOfferEventChecker(eventBus: EventBus) : EventChecker(eventBus) {
    private var cachedOffers = emptyList<GrandExchangeOffer>()

    override fun check() {
        val last = cachedOffers
        val current = GrandExchangeOffers.all().map { PoorlyNamedGrandExchangeOffer(
                it.itemsExchanged,
                it.totalOfferQuantity,
                it.coinsExchanged,
                it.state,
                it.itemPrice,
                it.itemId
        ) }
        try {
            val diff = Math.min(last.size, current.size)
            (0 until diff)
                    .filter { last[it] != current[it] }
                    .forEach { eventBus.post(GrandExchangeOfferUpdated(it)) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        cachedOffers = current
    }
}
package rs.helpkit.internal.event

import com.google.common.eventbus.EventBus
import rs.helpkit.api.game.GEOffer
import rs.helpkit.api.game.GrandExchange
import rs.helpkit.api.game.GETransaction
import rs.helpkit.api.game.listener.event.GEOfferUpdated

/**
 * @since 03/20/2018
 */
class GEOfferEventChecker(eventBus: EventBus) : EventChecker(eventBus) {
    private var cachedOffers = emptyList<GEOffer>()

    override fun check() {
        val last = cachedOffers
        val current = GrandExchange.offers().map {
            GETransaction(
                    it.itemsExchanged,
                    it.totalOfferQuantity,
                    it.coinsExchanged,
                    it.state,
                    it.itemPrice,
                    it.itemId
            )
        }
        try {
            val diff = Math.min(last.size, current.size)
            (0 until diff)
                    .filter { last[it] != current[it] }
                    .forEach { eventBus.post(GEOfferUpdated(it)) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        cachedOffers = current
    }
}
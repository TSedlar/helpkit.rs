package rs.helpkit.api.game

import rs.helpkit.api.game.wrapper.OfferBox
import rs.helpkit.api.raw.Fields

/**
 * @since 03/21/2018
 */
object GrandExchange {
    operator fun get(slot: Int): GEOffer? {
        return offers()[slot]
    }

    fun offers(): List<GEOffer> {
        val offers = Fields["Client#exchangeOffers"]
        if (offers is Array<*>) {
            return offers.map { OfferBox(it) }
        }
        return emptyList()
    }

    fun activeOffers(): List<GEOffer> {
        return offers().filter { it.itemId > 0 }
    }
}
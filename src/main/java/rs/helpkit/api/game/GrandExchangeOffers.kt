package rs.helpkit.api.game

import rs.helpkit.api.game.wrapper.OfferBox
import rs.helpkit.api.raw.Fields

/**
 * @since 03/21/2018
 */
object GrandExchangeOffers {
    operator fun get(slot: Int): GrandExchangeOffer? {
        return all()[slot]
    }

    fun all(): List<GrandExchangeOffer> {
        val offers = Fields["Client#grandExchangeOffers"]
        if (offers is Array<*>) {
            return offers.map { OfferBox(it) }
        }
        return emptyList()
    }

    fun active(): List<GrandExchangeOffer> {
        return all().filter { it.itemId > 0 }
    }
}
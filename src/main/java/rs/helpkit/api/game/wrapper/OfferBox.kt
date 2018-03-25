package rs.helpkit.api.game.wrapper

import rs.helpkit.api.game.data.GEOffer
import rs.helpkit.api.raw.Wrapper


/**
 * @since 03/21/2018
 */
class OfferBox(referant: Any?) : Wrapper("GrandExchangeOffer", referant), GEOffer {
    override val itemsExchanged: Int
        get() = asInt("itemsExchanged")

    override val totalOfferQuantity: Int
        get() = asInt("totalOfferQuantity")

    override val coinsExchanged: Int
        get() = asInt("coinsExchanged")

    override val state: Int
        get() = asInt("state")

    override val itemPrice: Int
        get() = asInt("itemPrice")

    override val itemId: Int
        get() = asInt("itemId")

    override fun toString(): String {
        return "OfferBox{itemPrice=$itemPrice, " +
                "itemId=$itemId, " +
                "itemsExchanged=$itemsExchanged, " +
                "totalOfferQuantity=$totalOfferQuantity, " +
                "coinsExchanged=$coinsExchanged, " +
                "state=$state}"
    }
}
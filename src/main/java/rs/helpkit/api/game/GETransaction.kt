package rs.helpkit.api.game

internal data class GETransaction(
        override val itemsExchanged: Int,
        override val totalOfferQuantity: Int,
        override val coinsExchanged: Int,
        override val state: Int,
        override val itemPrice: Int,
        override val itemId: Int
) : GEOffer
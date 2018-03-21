package rs.helpkit.api.game

interface GEOffer {
    val itemsExchanged: Int
    val totalOfferQuantity: Int
    val coinsExchanged: Int
    val state: Int
    val itemPrice: Int
    val itemId: Int
}

internal data class GETransaction(
        override val itemsExchanged: Int,
        override val totalOfferQuantity: Int,
        override val coinsExchanged: Int,
        override val state: Int,
        override val itemPrice: Int,
        override val itemId: Int
) : GEOffer

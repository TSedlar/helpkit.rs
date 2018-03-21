package rs.helpkit.api.game

interface GrandExchangeOffer {
    val itemsExchanged: Int
    val totalOfferQuantity: Int
    val coinsExchanged: Int
    val state: Int
    val itemPrice: Int
    val itemId: Int
}

internal data class PoorlyNamedGrandExchangeOffer(
        override val itemsExchanged: Int,
        override val totalOfferQuantity: Int,
        override val coinsExchanged: Int,
        override val state: Int,
        override val itemPrice: Int,
        override val itemId: Int
) : GrandExchangeOffer

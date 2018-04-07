package rs.helpkit.api.game.wrapper

import rs.helpkit.api.raw.Wrapper

class RSItem(
        storage: ItemStorage, val idx: Int, val id: Int, val stack: Int, node: Any?
): Wrapper("ItemDefinition", node) {

    val name
        get() = asString("name")

    val members
        get() = asBoolean("membersOnly")

    val storeValue
        get() = asInt("storeValue")

    val inventoryActions
        get() = asStringArray("inventoryActions")

    val groundActions
        get() = asStringArray("groundActions")

    val baseColors
        get() = asShortArray("baseColors")

    val texturedColors
        get() = asShortArray("texturedColors")
}
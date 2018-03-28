package rs.helpkit.api.game

import rs.helpkit.api.game.wrapper.Sprite
import rs.helpkit.api.raw.Methods

/**
 * @since 03/23/2018
 */
object ItemManager {
    private const val DEFAULT_SHADOW_COLOR = 3153952

    // 3153952
    fun createSprite(itemId: Int, quantity: Int = 0, stackable: Boolean = false): Sprite {

        val sprite: Any = Methods.invoke("Client#createSprite", null,
                // method params
                itemId,
                quantity,
                // border
                1,
                DEFAULT_SHADOW_COLOR,
                // stackable
                if (stackable) 1 else 0,
                // noted
                false
        ) ?: error("uh oh")
        return Sprite(sprite)
    }
}
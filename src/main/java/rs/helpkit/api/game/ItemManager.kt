package rs.helpkit.api.game

import rs.helpkit.api.raw.Methods
import java.awt.image.BufferedImage

/**
 * @since 03/23/2018
 */
object ItemManager {
    private const val DEFAULT_SHADOW_COLOR = 3153952

    // 3153952
    fun createSprite(itemId: Int, quantity: Int = 0, stackable: Boolean = false): BufferedImage? {
        val sprite: Any? = Methods.invoke("Client#createSprite", null, itemId, quantity, 1, DEFAULT_SHADOW_COLOR, if (stackable) 1 else 0, false, 512)
        return null
    }
}
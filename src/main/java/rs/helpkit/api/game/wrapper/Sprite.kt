package rs.helpkit.api.game.wrapper

import rs.helpkit.api.raw.Wrapper
import java.awt.image.BufferedImage

/**
 * @since 03/23/2018
 */
class Sprite(referent: Any?) : Wrapper("Sprite", referent) {
    fun pixels(): IntArray? = asIntArray("pixels")
    fun height(): Int = asInt("height")
    fun width(): Int = asInt("width")

    fun toBufferedImage(): BufferedImage {
        val width = width()
        val height = height()
        val pixels = pixels()!!
        val transPixels = IntArray(pixels.size)
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        for (i in pixels.indices) {
            if (pixels[i] != 0) {
                transPixels[i] = pixels[i] or -0x1000000
            }
        }

        img.setRGB(0, 0, width, height, transPixels, 0, width)
        return img
    }
}
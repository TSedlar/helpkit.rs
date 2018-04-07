package rs.helpkit.api.game.color

import rs.helpkit.api.raw.Fields
import java.awt.Color
import java.awt.image.BufferedImage

/**
 * @author Tyler Sedlar
 * @since 4/7/2018
 */
object Colors {

    fun rgbAt(x: Int, y: Int): Int {
        Fields["Client#interfaceProducer"]?.let {
            Fields["ComponentProducer#image", it].let {
                val img = it as BufferedImage
                return img.getRGB(x, y)
            }
        }
        return Color.BLACK.rgb
    }

    fun pixelAt(x: Int, y: Int): Pixel {
        val rgb = rgbAt(x, y)
        val r = rgb shr 16 and 255
        val g = rgb shr 8 and 255
        val b = rgb and 255
        return Pixel(r, g, b)
    }
}
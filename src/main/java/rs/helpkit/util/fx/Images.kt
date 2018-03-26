package rs.helpkit.util.fx

import java.awt.image.BufferedImage

/**
 * @author Tyler Sedlar
 * @since 3/26/2018
 */
object Images {

    fun toIndexedImage(image: BufferedImage): BufferedImage {
        val indexed = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
        val g = indexed.createGraphics()
        g.drawImage(image, 0, 0, null)
        g.dispose()
        return indexed
    }
}
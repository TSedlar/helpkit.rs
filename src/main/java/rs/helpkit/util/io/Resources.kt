package rs.helpkit.util.io

import java.awt.Font
import java.io.File
import java.awt.GraphicsEnvironment
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object Resources {

    val FONT_RS_SMALL = "RuneScape Small"
    val FONT_RS_BOLD_2 = "RuneScape Chat Bold 2"

    @JvmStatic
    fun installFonts() {
        registerFont("/fonts/runescape_small.ttf")
        registerFont("/fonts/runescape_chat_bold_2.ttf")
    }

    @JvmStatic
    fun registerFont(path: String) {
        val file: String? = Resources::class.java.getResource(path)?.file
        if (file != null) {
            val env = GraphicsEnvironment.getLocalGraphicsEnvironment()
            env.registerFont(Font.createFont(Font.TRUETYPE_FONT, File(file)))
        }
    }

    @JvmStatic
    fun img(path: String) : BufferedImage {
        return ImageIO.read(Resources::class.java.getResourceAsStream(path))
    }
}
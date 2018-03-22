package rs.helpkit.api.rsui

import rs.helpkit.util.io.Resources
import java.awt.Graphics2D
import java.awt.image.BufferedImage

/**
 * @author Tyler Sedlar
 * @since 3/21/2018
 */
class RSImage(x: Int, y: Int) : FXChildComponent() {

    var image: BufferedImage? = null

    init {
        this.x = x
        this.y = y
    }

    constructor(image: BufferedImage, x: Int, y: Int) : this(x, y) {
        this.image = image
        super.w = image.width
        super.h = image.height
    }

    constructor(path: String, x: Int, y: Int) : this(x, y) {
        this.image = Resources.img(path)
        if (image != null) {
            super.w = image!!.width
            super.h = image!!.height
        }
    }

    override fun render(g: Graphics2D, rx: Int, ry: Int) {
        if (image != null) {
            g.drawImage(image, rx + x + xOff, ry + y + yOff, null)
        }
    }
}
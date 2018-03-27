package rs.helpkit.api.rsui

import rs.helpkit.util.io.Resources
import java.awt.Graphics2D
import java.awt.image.BufferedImage

/**
 * @author Tyler Sedlar
 * @since 3/21/2018
 */
open class RSImage(x: Int, y: Int) : FXComponent() {

    var image: BufferedImage? = null
    var hoverImage: BufferedImage? = null

    protected var currentImage: BufferedImage? = null

    var disableHover = false

    init {
        this.x = x
        this.y = y
        onHover({ _, _ ->
            if (!disableHover) {
                if (hoverImage != null) {
                    currentImage = hoverImage
                }
            }
        }, {
            if (!disableHover) {
                currentImage = image
            }
        })
    }

    fun disableHover(): RSImage {
        disableHover = true
        return this
    }

    constructor(image: BufferedImage, hoverImage: BufferedImage?, x: Int, y: Int) : this(x, y) {
        this.image = image
        this.currentImage = image
        this.hoverImage = hoverImage
        super.w = image.width
        super.h = image.height
    }

    constructor(path: String, hoverPath: String?, x: Int, y: Int) : this(x, y) {
        this.image = Resources.img(path)
        this.currentImage = image
        hoverPath?.let { this.hoverImage = Resources.img(it) }
        if (image != null) {
            super.w = image!!.width
            super.h = image!!.height
        }
    }

    override fun render(g: Graphics2D, rx: Int, ry: Int) {
        if (disableHover) {
            g.drawImage(image, rx + x + xOff, ry + y + yOff, null)
        } else if (currentImage != null) {
            g.drawImage(currentImage, rx + x + xOff, ry + y + yOff, null)
        }
    }
}
package rs.helpkit.api.rsui

import rs.helpkit.api.util.Bind
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Rectangle

/**
 * @author Tyler Sedlar
 * @since 3/21/2018
 */
open class RSLabel(x: Int, y: Int) : FXComponent() {

    protected var text: Bind<String>? = null
    var color: Color = Color.WHITE
    var font: Font = Font.decode(null) // default font

    var alignment = "left"

    constructor(text: String, x: Int, y: Int) : this(x, y) {
        bindTo { text }
    }

    constructor(text: () -> String, x: Int, y: Int) : this(x, y) {
        this.text = Bind(text)
    }

    init {
        this.x = x
        this.y = y
    }

    fun bindTo(text: () -> String): RSLabel {
        this.text = Bind(text)
        return this
    }

    fun useFont(family: String, size: Int): RSLabel {
        this.font = Font(family, Font.PLAIN, size)
        return this
    }

    fun color(color: Color): RSLabel {
        this.color = color
        return this
    }

    fun leftAlign(): RSLabel {
        this.alignment = "left"
        return this
    }

    fun rightAlign(): RSLabel {
        this.alignment = "right"
        return this
    }

    fun text(): String? = text?.value()

    override fun bounds(): Rectangle {
        val superBounds = super.bounds()
        if (alignment == "right") {
            superBounds.x -= superBounds.width
        }
        return superBounds
    }

    override fun exactBounds(): Rectangle {
        val superBounds = super.exactBounds()
        if (alignment == "right") {
            superBounds.x -= superBounds.width
        }
        return superBounds
    }

    override fun render(g: Graphics2D, rx: Int, ry: Int) {
        val str = text?.value()
        super.w = g.fontMetrics.getStringBounds(str, g).width.toInt()
        super.h = g.getFontMetrics(font).height
        g.font = font
        var xMod = 0
        if (alignment == "right") {
            xMod = -(g.fontMetrics.stringWidth(str))
        }
        g.color = Color.BLACK
        g.drawString(str, rx + x + xOff + 1 + xMod - 1, ry + y + yOff + h - 1 - 1)
        g.color = color
        g.drawString(str, rx + x + xOff + 1 + xMod, ry + y + yOff + h - 1)
    }
}
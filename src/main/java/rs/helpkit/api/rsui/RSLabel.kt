package rs.helpkit.api.rsui

import rs.helpkit.api.util.Bind
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D

/**
 * @author Tyler Sedlar
 * @since 3/21/2018
 */
class RSLabel(x: Int, y: Int) : FXChildComponent() {

    private var text: Bind<String>? = null
    var color: Color = Color.WHITE
    var font: Font = Font.decode(null) // default font

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

    fun useFont(family: String, size: Int) : RSLabel {
        this.font = Font(family, Font.PLAIN, size)
        return this
    }

    fun text(): String? = text?.value()

    override fun render(g: Graphics2D, rx: Int, ry: Int) {
        val str = text?.value()
        super.w = g.fontMetrics.getStringBounds(str, g).width.toInt()
        super.h = g.fontMetrics.getStringBounds(str, g).height.toInt()
        g.color = color
        g.font = font
        g.drawString(str, rx + x + xOff + 1, ry + y + yOff + h - 1)
    }
}
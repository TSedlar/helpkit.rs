package rs.helpkit.api.rsui

import rs.helpkit.util.io.Resources
import java.awt.*
import java.awt.geom.Area

class RSButton(x: Int, y: Int, w: Int, h: Int) : RSLabel(x, y) {

    companion object {
        val TEXTURE = Resources.img("/images/textures/btn-texture.png")
        val FONT = Resources.FONT_RS_BOLD_2
    }

    private var currentColor: Color

    init {
        this.w = w
        this.h = h
        useFont(FONT, 16)
        this.currentColor = super.color
        onHover({ _, _ ->
            currentColor = super.color.darker()
        }, {
            currentColor = super.color
        })
    }

    override fun render(g: Graphics2D, rx: Int, ry: Int) {
        val x = rx + this.x + xOff
        val y = ry + this.y + yOff

        val area = Area(Rectangle(x, y, w, h))

        // subtract top left
        area.subtract(Area(Rectangle(x, y, 3, 2)))
        area.subtract(Area(Rectangle(x, y, 2, 3)))

        // subtract top right
        area.subtract(Area(Rectangle(x + w - 3, y, 3, 2)))
        area.subtract(Area(Rectangle(x + w - 2, y, 2, 3)))

        // subtract bottom left
        area.subtract(Area(Rectangle(x, y + h - 2, 3, 2)))
        area.subtract(Area(Rectangle(x, y + h - 3, 2, 3)))

        // subtract bottom right
        area.subtract(Area(Rectangle(x + w - 3, y + h - 2, 3, 2)))
        area.subtract(Area(Rectangle(x + w - 2, y + h - 3, 2, 3)))

        // draw border
        g.stroke = BasicStroke(3F)
        g.color = Color(46, 43, 35)
        g.draw(area)

        g.stroke = BasicStroke(2F)
        g.color = Color(114, 100, 81)
        g.draw(area)

        // fill inner
        g.paint = TexturePaint(TEXTURE, Rectangle(x, y, w, h))
        g.fill(area)

        g.paint = null
        g.font = super.font

        super.text?.value().let {
            val textX = (x + (w / 2) - (g.fontMetrics.stringWidth(it) / 2))
            val textY = (y + (h / 2) + (g.fontMetrics.getStringBounds(it, g).height.toInt() / 2.5).toInt())
            g.color = Color.BLACK
            g.drawString(it, textX + 1, textY + 1)
            g.color = currentColor
            g.drawString(it, textX, textY)
        }
    }

}
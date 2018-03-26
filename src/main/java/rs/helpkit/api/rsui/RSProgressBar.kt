package rs.helpkit.api.rsui

import java.awt.Color
import java.awt.Graphics2D

class RSProgressBar(x: Int, y: Int, w: Int, h: Int, private var progress: () -> Int): FXComponent() {

    init {
        this.x = x
        this.y = y
        this.w = w
        this.h = h
    }

    override fun render(g: Graphics2D, rx: Int, ry: Int) {
        g.color = Color.BLACK

        val x = this.x + rx
        val y = this.y + ry

        g.drawRect(x, y, w, h)

        val shadeHeight = ((h - 2) * 0.375).toInt()
        g.color = Color(47, 42, 36)
        g.fillRect(x + 1, y + 1, w - 1, shadeHeight)

        g.color = Color(58, 50, 44)
        g.fillRect(x + 1, y + 1 + shadeHeight, w - 1, h - 1 - shadeHeight)

        val progress = (progress() * (w - 1)) / 100
        if (progress == 100) {
            g.color = Color(0, 95, 0, 192)
        } else {
            g.color = Color(255, 152, 31, 192)
        }
        g.fillRect(x + 1, y + 1, progress, h - 1)
    }
}
package rs.helpkit.api.rsui

import java.awt.Color
import java.awt.Graphics2D

/**
 * @author Tyler Sedlar
 * @since 3/21/2018
 */
class RSFrame(w: Int, h: Int) : RSWindow(w, h) {

    init {
        super.w = w
        super.h = h
    }

    companion object {
        private var FRAME_BACKGROUND: Color = Color(62, 53, 41)
        private var FRAME_INNER_BORDER: Color = Color(58, 53, 42)
        private var FRAME_BORDER_1: Color = Color(96, 90, 76)
        private var FRAME_BORDER_2: Color = Color(83, 76, 62)
        private var FRAME_BORDER_3: Color = Color(80, 73, 57)
        private var FRAME_BORDER_4: Color = Color(87, 80, 67)
        private var FRAME_CORNER_OUTER: Color = Color(171, 167, 147)
        private var FRAME_CORNER_MID: Color = Color(148, 138, 122)
        private var FRAME_CORNER_INNER: Color = Color(118, 111, 95)
        private var FRAME_CORNER_BOLT_TOP: Color = Color(177, 174, 167)
        private var FRAME_CORNER_BOLT_BL: Color = Color(96, 90, 76)
        private var FRAME_CORNER_BOLT_BR: Color = Color(74, 60, 45)
        private var FRAME_PATCH_TOP: Color = Color(116, 106, 90)
        private var FRAME_PATCH_BOTTOM: Color = Color(107, 98, 84)
        private var FRAME_PATCH_LEFT: Color = Color(116, 106, 90)
        private var FRAME_PATCH_RIGHT: Color = Color(107, 98, 84)
    }

    override fun add(child: FXComponent) {
        super.add(child)
        child.xOff = 5
        child.yOff = 5
    }

    override fun render(g: Graphics2D, rx: Int, ry: Int) {
        g.color = Color.BLACK
        g.fillRect(rx, ry, w, h)
        g.color = FRAME_BACKGROUND
        g.fillRect(rx + 1, ry + 1, w - 2, h - 2)
        g.color = FRAME_INNER_BORDER
        g.drawRect(rx + 5, ry + 5, w - 7, h - 7)

        // Draw left
        g.color = FRAME_BORDER_1
        g.fillRect(rx + 1, ry + 1, 1, h - 2)
        g.color = FRAME_BORDER_2
        g.fillRect(rx + 2, ry + 1, 1, h - 2)
        g.color = FRAME_BORDER_3
        g.fillRect(rx + 3, ry + 1, 1, h - 2)
        g.color = FRAME_BORDER_4
        g.fillRect(rx + 4, ry + 1, 1, h - 2)

        // Draw right
        g.color = FRAME_BORDER_1
        g.fillRect(rx + 1 + w - 3, ry + 1, 1, h - 2)
        g.color = FRAME_BORDER_2
        g.fillRect(rx + 1 + w - 4, ry + 1, 1, h - 2)
        g.color = FRAME_BORDER_3
        g.fillRect(rx + 1 + w - 5, ry + 1, 1, h - 2)
        g.color = FRAME_BORDER_4
        g.fillRect(rx + 1 + w - 6, ry + 1, 1, h - 2)

        // Draw top
        g.color = FRAME_BORDER_1
        g.fillRect(rx + 1, ry + 1, w - 2, 1)
        g.color = FRAME_BORDER_2
        g.fillRect(rx + 1, ry + 2, w - 2, 1)
        g.color = FRAME_BORDER_3
        g.fillRect(rx + 1, ry + 3, w - 2, 1)
        g.color = FRAME_BORDER_4
        g.fillRect(rx + 1, ry + 4, w - 2, 1)

        // Draw bottom
        g.color = FRAME_BORDER_1
        g.fillRect(rx + 1, ry + 1 + h - 3, w - 2, 1)
        g.color = FRAME_BORDER_2
        g.fillRect(rx + 1, ry + 1 + h - 4, w - 2, 1)
        g.color = FRAME_BORDER_3
        g.fillRect(rx + 1, ry + 1 + h - 5, w - 2, 1)
        g.color = FRAME_BORDER_4
        g.fillRect(rx + 1, ry + 1 + h - 6, w - 2, 1)

        // Draw top left corner
        g.color = FRAME_CORNER_OUTER
        g.fillRect(rx + 1, ry + 1, 6, 1)
        g.fillRect(rx + 1, ry + 1, 1, 6)
        g.color = FRAME_CORNER_MID
        g.fillRect(rx + 2, ry + 2, 5, 1)
        g.fillRect(rx + 2, ry + 2, 1, 5)
        g.color = FRAME_CORNER_INNER
        g.fillRect(rx + 3, ry + 3, 3, 1)
        g.fillRect(rx + 3, ry + 3, 1, 3)
        g.fillRect(rx + 4, ry + 4, 1, 1)
        // Draw box in corner
        g.color = FRAME_CORNER_BOLT_TOP
        g.fillRect(rx + 2, ry + 2, 2, 1)
        g.color = FRAME_CORNER_BOLT_BL
        g.fillRect(rx + 2, ry + 3, 1, 1)
        g.color = FRAME_CORNER_BOLT_BR
        g.fillRect(rx + 3, ry + 3, 1, 1)

        // Draw top/bottom "patches"
        for (i in 1..(w / 20)) {
            g.color = FRAME_PATCH_TOP
            g.fillRect(rx + (i * 20), ry + 1, 4, 1)
            g.color = FRAME_PATCH_BOTTOM
            g.fillRect(rx + (i * 20), ry + 1 + h - 6, 4, 1)
        }

        // Draw side "patches"
        for (i in 1..(h / 20)) {
            g.color = FRAME_PATCH_LEFT
            g.fillRect(rx + 1, ry + (i * 20), 1, 4)
            g.color = FRAME_PATCH_RIGHT
            g.fillRect(rx + 1 + w - 6, ry + (i * 20), 1, 4)
        }

        // Draw bottom left corner
        g.color = FRAME_CORNER_OUTER
        g.fillRect(rx + 1, ry + 1 + h - 3, 6, 1)
        g.fillRect(rx + 1, ry + 1 + h - 8, 1, 6)
        g.color = FRAME_CORNER_MID
        g.fillRect(rx + 2, ry + 1 + h - 4, 5, 1)
        g.fillRect(rx + 2, ry + 1 + h - 8, 1, 5)
        g.color = FRAME_CORNER_INNER
        g.fillRect(rx + 3, ry + 1 + h - 5, 3, 1)
        g.fillRect(rx + 3, ry + 1 + h - 7, 1, 3)
        g.fillRect(rx + 4, ry + 1 + h - 6, 1, 1)
        // Draw box in corner
        g.color = FRAME_CORNER_BOLT_TOP
        g.fillRect(rx + 2, ry + 1 + h - 5, 2, 1)
        g.color = FRAME_CORNER_BOLT_BL
        g.fillRect(rx + 2, ry + 1 + h - 4, 1, 1)
        g.color = FRAME_CORNER_BOLT_BR
        g.fillRect(rx + 3, ry + 1 + h - 4, 1, 1)

        // Draw top right corner
        g.color = FRAME_CORNER_OUTER
        g.fillRect(rx + 1 + w - 8, ry + 1, 6, 1)
        g.fillRect(rx + 1 + w - 3, ry + 1, 1, 6)
        g.color = FRAME_CORNER_MID
        g.fillRect(rx + 1 + w - 8, ry + 2, 5, 1)
        g.fillRect(rx + 1 + w - 4, ry + 2, 1, 5)
        g.color = FRAME_CORNER_INNER
        g.fillRect(rx + 1 + w - 7, ry + 3, 3, 1)
        g.fillRect(rx + 1 + w - 5, ry + 3, 1, 3)
        g.fillRect(rx + 1 + w - 6, ry + 4, 1, 1)
        // Draw box in corner
        g.color = FRAME_CORNER_BOLT_TOP
        g.fillRect(rx + 1 + w - 6, ry + 2, 2, 1)
        g.color = FRAME_CORNER_BOLT_BL
        g.fillRect(rx + 1 + w - 4, ry + 3, 1, 1)
        g.color = FRAME_CORNER_BOLT_BR
        g.fillRect(rx + 1 + w - 4, ry + 3, 1, 1)

        // Draw bottom right corner
        g.color = FRAME_CORNER_OUTER
        g.fillRect(rx + 1 + w - 8, ry + 1 + h - 3, 6, 1)
        g.fillRect(rx + 1 + w - 3, ry + 1 + h - 8, 1, 6)
        g.color = FRAME_CORNER_MID
        g.fillRect(rx + 1 + w - 8, ry + 1 + h - 4, 5, 1)
        g.fillRect(rx + 1 + w - 4, ry + 1 + h - 8, 1, 5)
        g.color = FRAME_CORNER_INNER
        g.fillRect(rx + 1 + w - 7, ry + 1 + h - 5, 3, 1)
        g.fillRect(rx + 1 + w - 5, ry + 1 + h - 7, 1, 3)
        g.fillRect(rx + 1 + w - 6, ry + 1 + h - 6, 1, 1)
        // Draw box in corner
        g.color = FRAME_CORNER_BOLT_TOP
        g.fillRect(rx + 1 + w - 6, ry + 1 + h - 6, 2, 1)
        g.color = FRAME_CORNER_BOLT_BL
        g.fillRect(rx + 1 + w - 4, ry + 1 + h - 5, 1, 1)
        g.color = FRAME_CORNER_BOLT_BR
        g.fillRect(rx + 1 + w - 4, ry + 1 + h - 5, 1, 1)
    }
}
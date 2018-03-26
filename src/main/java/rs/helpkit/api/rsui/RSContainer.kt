package rs.helpkit.api.rsui

import java.awt.Graphics2D

/**
 * @author Tyler Sedlar
 * @sicne 3/26/2018
 */
open class RSContainer(val parent: RSWindow): RSWindow(parent.x, parent.y) {

    override fun render(g: Graphics2D, rx: Int, ry: Int) {
    }
}
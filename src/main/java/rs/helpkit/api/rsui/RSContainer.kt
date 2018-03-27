package rs.helpkit.api.rsui

import java.awt.Graphics2D

/**
 * @author Tyler Sedlar
 * @sicne 3/26/2018
 */
open class RSContainer(x: Int, y: Int) : RSWindow(x, y) {

    constructor() : this(0, 0)

    override fun render(g: Graphics2D, rx: Int, ry: Int) {
    }
}
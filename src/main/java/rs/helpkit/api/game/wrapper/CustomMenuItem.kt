package rs.helpkit.api.game.wrapper

import java.awt.Rectangle

open class CustomMenuItem(val text: String, val handler: () -> Unit) {

    var bounds: Rectangle? = null
}
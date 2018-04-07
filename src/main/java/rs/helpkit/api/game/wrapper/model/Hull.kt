package rs.helpkit.api.game.wrapper.model

import java.awt.*

/**
 * @author Tyler Sedlar
 * @since 4/7/2018
 */
class Hull : Polygon() {

    fun valid(): Boolean {
        return npoints >= 3
    }
}
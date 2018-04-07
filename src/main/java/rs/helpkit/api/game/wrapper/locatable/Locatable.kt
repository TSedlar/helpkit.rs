package rs.helpkit.api.game.wrapper.locatable

import rs.helpkit.api.game.access.Client
import rs.helpkit.api.game.wrapper.model.Hull

interface Locatable {

    fun hull(left: Int, right: Int, bottom: Int, top: Int, levitation: Int, height: Int): Hull

    val height: Int

    val regionX: Int

    val regionY: Int

    val localizedX
        get() = regionX shr 7

    val localizedY
        get() = regionY shr 7

    val worldX
        get() = Client.regionBaseX() + localizedX

    val worldY
        get() = Client.regionBaseY() + localizedY

    fun plane(): Int {
        return Client.currentPlane()
    }
}
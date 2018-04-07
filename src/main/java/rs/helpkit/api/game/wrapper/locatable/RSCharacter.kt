package rs.helpkit.api.game.wrapper.locatable

import rs.helpkit.api.game.access.Client
import rs.helpkit.api.game.access.Projection
import rs.helpkit.api.game.wrapper.model.Hull
import rs.helpkit.api.raw.Fields
import rs.helpkit.api.raw.Wrapper

/**
 * @author Tyler Sedlar
 * @since 4/7/2018
 */
open class RSCharacter(parentContainer: String, referent: Any?) : Wrapper(parentContainer, referent), Locatable {

    override val height: Int
        get() = Fields.asInt("Renderable#modelHeight", get());

    override val regionX: Int
        get() = Fields.asInt("Character#regionX", get())

    override val regionY: Int
        get() = Fields.asInt("Character#regionY", get())

    override fun hull(left: Int, right: Int, bottom: Int, top: Int, levitation: Int, height: Int): Hull {
        return Projection.hull(regionX - 64, regionY - 64, Client.currentPlane(), left, right, bottom,
                top, levitation, height)
    }

    fun hull(): Hull {
        return hull(15, 15, 10, 18, 0, (height * 0.8).toInt())
    }
}

package rs.helpkit.api

import rs.helpkit.api.util.Renderable

abstract class RenderPlugin : Plugin(), Renderable {

    override fun loop(): Int {
        return -1
    }
}

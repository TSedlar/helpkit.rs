package rs.helpkit.api.game.wrapper

import rs.helpkit.api.raw.Wrapper

class Nameable(referent: Any?) : Wrapper("Nameable", referent) {

    fun text(): String? = asString("text")
}
package rs.helpkit.api.game.wrapper.node

import rs.helpkit.api.raw.Fields
import rs.helpkit.api.raw.Wrapper

class RSNode(referent: Any?) : Wrapper("Node", referent) {

    fun next(): RSNode = RSNode(this["next"])

    fun previous(): RSNode = RSNode(this["previous"])

    fun uid(): Long = asLong("uid")

    fun subWindowId(): Int {
        return try {
            Fields.asInt("SubWindow#targetWindowId", get())
        } catch (e: Exception) {
            -1
        }
    }
}
package rs.helpkit.api.game.wrapper

import rs.helpkit.api.raw.Wrapper
import java.io.InputStream
import java.io.OutputStream

class RSSocket(referent: Any?) : Wrapper("RSSocket", referent) {

    fun input(): InputStream? = this["input"] as InputStream

    fun output(): OutputStream? = this["output"] as OutputStream
}
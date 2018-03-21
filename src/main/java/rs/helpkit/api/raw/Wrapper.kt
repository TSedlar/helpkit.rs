package rs.helpkit.api.raw

import java.lang.ref.WeakReference

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
open class Wrapper(private val container: String, referent: Any?) : WeakReference<Any>(referent) {

    private fun key(name: String): String = container + "#" + name

    protected operator fun get(name: String): Any? = Fields[key(name), get()]

    protected fun asInt(name: String): Int = Fields.asInt(key(name), get())

    protected fun asString(name: String): String? = Fields.asString(key(name), get())

    protected fun asIntArray(name: String): IntArray? = Fields.asIntArray(key(name), get())
}

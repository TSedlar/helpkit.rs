package rs.helpkit.api.raw

import rs.helpkit.internal.HookLoader
import java.lang.ref.WeakReference

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
open class Wrapper(private val container: String, referent: Any?) : WeakReference<Any>(referent) {

    private fun key(name: String): String = "$container#$name"

    protected operator fun get(name: String): Any? = Fields[key(name), get()]

    protected fun asInt(name: String): Int = Fields.asInt(key(name), get())

    protected fun asLong(name: String): Long = Fields.asLong(key(name), get())

    protected fun asBoolean(name: String): Boolean = Fields.asBoolean(key(name), get())

    protected fun asString(name: String): String? = Fields.asString(key(name), get())

    protected fun asArray(name: String): Array<*>? = Fields.asArray(key(name), get())

    protected fun asIntArray(name: String): IntArray? = Fields.asIntArray(key(name), get())

    protected fun asStringArray(name: String): Array<String?>? = Fields.asStringArray(key(name), get())

    fun validate(): Boolean = get() != null

    companion object {

        fun load(className: String, vararg args: Any?): Any? {
            val clazz = HookLoader.CLASSES[className]
            if (clazz != null) {
                val constructor = clazz.constructors[0]
                return if (constructor.parameterCount == 0) {
                    constructor.newInstance()
                } else {
                    constructor.newInstance(args)
                }
            }
            return null
        }
    }
}

package rs.helpkit.api.raw

import rs.helpkit.internal.HookLoader

/**
 * @author Tyler Sedlar
 * @since 3/22/2018
 */
object Methods {

    /**
     * Gets the method's value of the given hook with its parent
     *
     * @param key    The key of the hook to get a value for
     * @param parent The parent, if it exists, otherwise <tt>null</tt>.
     * @return The methods's value
     */
    @JvmOverloads
    operator fun invoke(key: String, parent: Any? = null, vararg arguments: Any?): Any? {
        val handle = HookLoader.METHODS[key]
        if (handle == null) {
            return null
        } else {
            val parameters: MutableList<Any?> = ArrayList()
            if (parent != null) {
                parameters.add(parent)
            }
            parameters.addAll(arguments)
            if (key in HookLoader.OPAQUES) {
                parameters.add(HookLoader.OPAQUES[key]?.value())
            }
            return try {
                handle.invokeWithArguments(parameters)
            } catch (t: Throwable) {
                t.printStackTrace()
                null
            }
        }
    }
}
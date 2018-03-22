package rs.helpkit.api.raw

import rs.helpkit.internal.HookLoader

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object Fields {

    /**
     * Sets the field's value of the given hook with its parent
     *
     * @param key    The key of the hook to set a value for
     * @param value  The value to set the field to
     * @param parent The parent, if it exists, otherwise <tt>null</tt>.
     */
    @JvmOverloads
    fun set(key: String, value: Any, parent: Any? = null) {
        if (HookLoader.DIRECT_FIELDS.containsKey(key)) {
            val field = HookLoader.DIRECT_FIELDS[key]
            try {
                field?.set(parent, value)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Gets the field's value of the given hook with its parent
     *
     * @param key    The key of the hook to get a value for
     * @param parent The parent, if it exists, otherwise <tt>null</tt>.
     * @return The field's value
     */
    @JvmOverloads
    operator fun get(key: String, parent: Any? = null): Any? {
        val handle = HookLoader.FIELDS[key]
        if (handle == null) {
            return null
        } else {
            var value: Any? = try {
                if (parent != null) {
                    handle.invokeWithArguments(parent)
                } else {
                    handle.invokeWithArguments()
                }
            } catch (t: Throwable) {
                null
            }
            if (HookLoader.MULTIPLIERS.containsKey(key) && value != null) {
                val isLong = HookLoader.LONG_MAP[key] ?: false
                value = if (isLong) {
                    value as Long * HookLoader.MULTIPLIERS[key]!!.toLong()
                } else {
                    value as Int * HookLoader.MULTIPLIERS[key]!!.toInt()
                }
            }
            return value
        }
    }

    @JvmOverloads
    fun asInt(key: String, parent: Any? = null): Int {
        return try {
            get(key, parent) as Int
        } catch (e: Exception) {
            -1
        }
    }

    @JvmOverloads
    fun asLong(key: String, parent: Any? = null): Long {
        return try {
            get(key, parent) as Long
        } catch (e: Exception) {
            -1
        }
    }

    @JvmOverloads
    fun asBoolean(key: String, parent: Any? = null): Boolean {
        return try {
            get(key, parent) as Boolean
        } catch (e: Exception) {
            false
        }
    }

    @JvmOverloads
    fun asString(key: String, parent: Any? = null): String? {
        return get(key, parent)?.toString()
    }

    @JvmOverloads
    fun asArray(key: String, parent: Any? = null): Array<*>? {
        return try {
            get(key, parent) as Array<*>
        } catch (e: Exception) {
//            e.printStackTrace()
            emptyArray<Any>()
        }
    }

    @Suppress("UNCHECKED_CAST")
    @JvmOverloads
    fun asIntArray(key: String, parent: Any? = null): IntArray? {
        return try {
            get(key, parent) as IntArray
        } catch (e: Exception) {
//            e.printStackTrace()
            IntArray(0)
        }
    }

    @JvmOverloads
    fun asStringArray(name: String, parent: Any? = null): Array<String>? {
        return asArray(name, parent)?.map { item -> item.toString() }?.toTypedArray()
    }
}

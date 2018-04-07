package rs.helpkit.api.raw

import rs.helpkit.internal.HookLoader
import java.math.BigInteger


/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object Fields {

    private val X64_BIG_INT = BigInteger((1L shl 32).toString())

    /**
     * Sets the field's value of the given hook with its parent
     *
     * @param key    The key of the hook to set a value for
     * @param value  The value to set the field to
     * @param parent The parent, if it exists, otherwise <tt>null</tt>.
     */
    @JvmOverloads
    fun set(key: String, value: Any?, parent: Any? = null) {
        if (HookLoader.DIRECT_FIELDS.containsKey(key)) {
            val field = HookLoader.DIRECT_FIELDS[key]
            try {
                field?.set(parent, value)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }

    private fun setNumber(key: String, transform: (number: Int) -> Number, parent: Any? = null) {
        if (key in HookLoader.DIRECT_FIELDS) {
            try {
                var encoder = 1
                if (key in HookLoader.MULTIPLIERS) {
                    encoder = BigInteger.valueOf(HookLoader.MULTIPLIERS[key]!!.toLong())
                            .modInverse(X64_BIG_INT).toInt()
                }
                HookLoader.DIRECT_FIELDS[key].let {
                    it?.set(parent, transform(encoder))
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    @JvmOverloads
    fun setInt(key: String, value: Int, parent: Any? = null) {
        setNumber(key, { (it * value) }, parent)
    }

    @JvmOverloads
    fun setShort(key: String, value: Short, parent: Any? = null) {
        setNumber(key, { (it * value).toShort() }, parent)
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
            println("don't think so.. $key")
            return null
        } else {
            var value: Any? = try {
                if (parent != null) {
                    if (key == "PacketContext#packet") {
//                        println("??")
//                        println(HookLoader.DIRECT_FIELDS[key]!!.get(parent))
                    }
                    handle.invokeWithArguments(parent)
                } else {
                    handle.invokeWithArguments()
                }
            } catch (t: Throwable) {
//                t.printStackTrace()
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
    fun asShort(key: String, parent: Any? = null): Short {
        return try {
            get(key, parent) as Short
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

    @Suppress("UNCHECKED_CAST")
    @JvmOverloads
    fun asShortArray(key: String, parent: Any? = null): ShortArray? {
        return try {
            get(key, parent) as ShortArray
        } catch (e: Exception) {
//            e.printStackTrace()
            ShortArray(0)
        }
    }

    @JvmOverloads
    fun asStringArray(name: String, parent: Any? = null): Array<String?>? {
        return asArray(name, parent)?.map { item -> item.toString() }?.toTypedArray()
    }
}

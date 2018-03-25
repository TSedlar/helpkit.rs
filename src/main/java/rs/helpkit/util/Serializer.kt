package rs.helpkit.util

import com.google.gson.Gson

/**
 * @author Tyler Sedlar
 * @since 3/24/2018
 */
object Serializer {

    private val GSON = Gson()

    fun serializeObject(o: Any): String = GSON.toJson(o)

    fun unserializeObject(s: String, o: Any): Any = GSON.fromJson(s, o.javaClass)

    fun cloneObject(o: Any): Any = unserializeObject(serializeObject(o), o)
}
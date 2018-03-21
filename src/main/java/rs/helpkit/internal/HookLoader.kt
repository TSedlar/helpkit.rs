package rs.helpkit.internal

import rs.helpkit.pref.HKConfig
import rs.helpkit.reflect.Classes
import rs.helpkit.util.io.TSVListing

import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.reflect.Field
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object HookLoader {

    val INVERSE_FIELD_MAP: MutableMap<String, String> = ConcurrentHashMap()
    val LONG_MAP: MutableMap<String, Boolean> = ConcurrentHashMap()

    val FIELDS: MutableMap<String, MethodHandle> = ConcurrentHashMap()
    val DIRECT_FIELDS: MutableMap<String, Field> = ConcurrentHashMap()
    val MULTIPLIERS: MutableMap<String, Number> = ConcurrentHashMap()

    fun load(loader: ClassLoader) {
        loadFields(loader)
        loadMultipliers()
    }

    private fun loadFields(loader: ClassLoader) {
        val fields = TSVListing.load(HKConfig.path(HKConfig.DATA, "fields.tsv"))
        fields.lines.removeAt(0) // remove header
        fields.lines.parallelStream().forEach { line ->
            val key = line[0]
            if (line[1] == "null") {
                println("BROKEN HOOK @ " + key)
            } else {
                INVERSE_FIELD_MAP[line[1]] = line[0]
                val splits = line[1].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val className = splits[0]
                val fieldName = splits[1]
                try {
                    val clazz = loader.loadClass(className)
                    val field = Classes.findField(clazz, { it.name == fieldName })
                    if (field == null) {
                        println("HOOK NOT FOUND @ " + key)
                    } else {
                        LONG_MAP[key] = field.type == Long::class.javaPrimitiveType
                        DIRECT_FIELDS[key] = field
                        FIELDS[key] = MethodHandles.lookup().unreflectGetter(field)
                    }
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun loadMultipliers() {
        val multipliers = TSVListing.load(HKConfig.path(HKConfig.DATA, "multipliers.tsv"))
        multipliers.lines.removeAt(0) // remove header
        multipliers.lines.parallelStream().forEach { line ->
            if (INVERSE_FIELD_MAP.containsKey(line[0])) {
                val key = INVERSE_FIELD_MAP[line[0]] ?: return@forEach
                if (key in LONG_MAP) {
                    MULTIPLIERS[key] = java.lang.Long.parseLong(line[1])
                } else {
                    MULTIPLIERS[key] = Integer.parseInt(line[1])
                }
            }
        }
    }
}

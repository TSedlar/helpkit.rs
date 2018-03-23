package rs.helpkit.internal

import rs.helpkit.pref.HKConfig
import rs.helpkit.reflect.Classes
import rs.helpkit.util.io.TSVListing

import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.reflect.Field
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object HookLoader {

    val INVERSE_FIELD_MAP: MutableMap<String, String> = ConcurrentHashMap()

    val LONG_MAP: MutableMap<String, Boolean> = ConcurrentHashMap()

    val CLASSES: MutableMap<String, Class<*>> = ConcurrentHashMap()
    val FIELDS: MutableMap<String, MethodHandle> = ConcurrentHashMap()
    val METHODS: MutableMap<String, MethodHandle> = ConcurrentHashMap()

    val DIRECT_FIELDS: MutableMap<String, Field> = ConcurrentHashMap()

    val MULTIPLIERS: MutableMap<String, Number> = ConcurrentHashMap()
    val OPAQUES: MutableMap<String, Opaque> = ConcurrentHashMap()

    fun load(loader: ClassLoader) {
        loadFields(loader)
        loadMultipliers()
        loadOpaques()
        loadMethods(loader)
    }

    private fun loadFields(loader: ClassLoader) {
        val fields = TSVListing.load(HKConfig.path(HKConfig.DATA, "fields.tsv"))
        fields.lines.removeAt(0) // remove header
        fields.lines.parallelStream().forEach { line ->
            if (!line.isEmpty() && line.size >= 2) {
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
                        CLASSES.put(key.split("#")[0], clazz)
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

    private fun loadOpaques() {
        val opaques = TSVListing.load(HKConfig.path(HKConfig.DATA, "opaques.tsv"))
        opaques.lines.removeAt(0) // remove header
        opaques.lines.parallelStream().forEach { line ->
            OPAQUES[line[0]] = Opaque(line[1].toLong(), "long")
        }
    }

    private fun loadMethods(loader: ClassLoader) {
        val methods = TSVListing.load(HKConfig.path(HKConfig.DATA, "methods.tsv"))
        methods.lines.removeAt(0)
        methods.lines.stream().forEach { line ->
            val key = line[0]
            if (line[1] == "null") {
                println("BROKEN HOOK @ " + key)
            } else {
                val splits = line[1].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val className = splits[0]
                val methodKey = splits[1]
                try {
                    val clazz = loader.loadClass(className)
                    val hasOpaque = line[1] in OPAQUES
                    var matchedOpaqueType: String? = null
                    val method = Classes.findMethod(clazz, { method ->
                        var str = method.name + "("
                        method.genericParameterTypes.forEachIndexed { idx, type ->
                            if (hasOpaque && idx == method.genericParameterTypes.size - 1) {
                                matchedOpaqueType = translate(type.typeName)
                            } else {
                                if (idx > 0) {
                                    str += ", "
                                }
                                str += translate(type.typeName)
                            }
                        }
                        str += ")"
                        return@findMethod str == methodKey
                    })
                    if (method != null) {
                        if (hasOpaque) {
                            OPAQUES[line[1]]?.type = matchedOpaqueType!!
                            OPAQUES[key] = OPAQUES[line[1]]!!
                        }
                        METHODS[key] = MethodHandles.lookup().unreflect(method)
                    }
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun translate(type: String): String {
        KNOWN_DESCS.forEach { desc ->
            if (type.contains(desc)) {
                return desc
            }
        }
        return type
    }

    private val KNOWN_DESCS = Arrays.asList(
            "int", "byte", "short", "long", "float", "boolean", "double", "char",
            "HashMap", "OutputStream", "GarbageCollectorMXBean", "MouseWheelListener",
            "String", "Image", "MouseMotionListener", "FocusEvent", "EventQueue",
            "Comparator", "LinkedHashMap", "Container", "Random", "List", "AudioFormat",
            "ClassNotFoundException", "HashSet", "ScheduledExecutorService", "Canvas",
            "CharSequence", "Method", "LinkedList", "Applet", "MouseWheelEvent",
            "RandomAccessFile", "Throwable", "SourceDataLine", "BigInteger", "Object",
            "Class", "Inflater", "FocusListener", "File", "Map", "Frame", "Iterable",
            "CRC32", "LineUnavailableException", "URL", "Font", "KeyEvent", "Field",
            "MouseListener", "RuntimeException", "FontMetrics", "Iterator", "Component",
            "Queue", "WindowListener", "MouseEvent", "Hashtable", "InputStream", "Calendar",
            "IOException", "ByteBuffer", "Exception", "Thread", "Graphics", "Integer", "Runnable",
            "WindowEvent", "KeyListener", "Clipboard", "Socket", "DataInputStream",
            "ClassNotFoundException", "InvalidClassException", "StreamCorruptedException",
            "OptionalDataException", "IllegalAccessException", "IllegalArgumentException",
            "InvocationTargetException", "SecurityException", "IOException", "NullPointerException",
            "RuntimeException"
    )
}

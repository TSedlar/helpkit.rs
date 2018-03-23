package rs.helpkit.internal

class Opaque(private val number: Number, var type: String) {

    fun value(): Any? {
        return when (type) {
            "int" -> number.toInt()
            "long" -> number.toLong()
            "byte" -> number.toByte()
            "short" -> number.toShort()
            else -> null
        }
    }
}
package rs.helpkit.api.util

class Bind<T> {

    private var value: Any? = null

    constructor(value: T) {
        this.value = value
    }

    constructor(value: () -> T) {
        this.value = value
    }

    @Suppress("UNCHECKED_CAST")
    fun value(): T {
        return if (value is Function<*>) {
            (value as () -> T).invoke()
        } else {
            value as T
        }
    }
}
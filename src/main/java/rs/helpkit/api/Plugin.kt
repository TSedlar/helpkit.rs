package rs.helpkit.api

import rs.helpkit.api.util.Time

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
abstract class Plugin : Runnable {

    var enabled = true

    abstract fun validate(): Boolean

    abstract fun loop(): Int

    override fun run() {
        var delay = 0
        do {
            if (!validate()) {
                delay = 1000
            } else {
                Time.sleep(delay.toLong())
                try {
                    delay = loop()
                } catch (e: Exception) {
                    e.printStackTrace()
                    break
                }

            }
        } while (delay >= 0)
    }
}

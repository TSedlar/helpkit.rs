package rs.helpkit.api

import rs.helpkit.api.util.Schedule
import rs.helpkit.api.util.Time

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
abstract class Plugin : Thread() {

    var enabled = true

    abstract fun validate(): Boolean

    override fun run() {
        val self: Plugin = this
        this.javaClass.methods.forEach { method ->
            val schedule = method.getAnnotation(Schedule::class.java)
            if (schedule != null) {
                Thread({
                    var errored = false
                    while (!errored) {
                        try {
                            if (enabled && validate()) {
                                method.invoke(self)
                                Time.sleep(schedule.interval.toLong())
                            } else {
                                Time.sleep(1000)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            errored = true
                        }
                    }
                }).start()
            }
        }
    }
}

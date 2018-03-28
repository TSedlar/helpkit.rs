package rs.helpkit.api

import com.google.common.eventbus.EventBus
import rs.helpkit.api.util.Schedule
import rs.helpkit.api.util.Time
import java.awt.event.MouseEvent
import javax.swing.event.MouseInputListener

fun hasManifest(clazz: Class<*>): Boolean = clazz.getAnnotation(Manifest::class.java) != null

fun Plugin.manifest(): Manifest? {
    return this.javaClass.getAnnotation(Manifest::class.java)
}

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
abstract class Plugin : Thread(), MouseInputListener {

    var enabled = true

    abstract fun validate(): Boolean

    open fun onEventBus(bus: EventBus) {
        //
    }

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

    override fun mouseReleased(e: MouseEvent) {
    }

    override fun mouseMoved(e: MouseEvent) {
    }

    override fun mouseEntered(e: MouseEvent) {
    }

    override fun mouseDragged(e: MouseEvent) {
    }

    override fun mouseClicked(e: MouseEvent) {
    }

    override fun mouseExited(e: MouseEvent) {
    }

    override fun mousePressed(e: MouseEvent) {
    }
}

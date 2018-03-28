package rs.helpkit.internal

import rs.helpkit.OSRSContainer
import rs.helpkit.api.game.access.Camera
import rs.helpkit.api.game.access.Client
import rs.helpkit.api.game.access.GameMenu
import java.io.FilterInputStream
import java.io.InputStream

/**
 * @author Tyler Sedlar
 * @since 3/24/2018
 */
class RSInputStream(var container: OSRSContainer, original: InputStream) : FilterInputStream(original) {

    private var prevCycle = 0

    override fun available(): Int {
        try {
            val currCycle = Client.cycle()
            if (prevCycle != currCycle) {
                onCycle()
                prevCycle = currCycle
            }
            onPacketReceived()
        } catch (e: Exception) {
        }
        return super.available()
    }

    fun onCycle() {
        container.plugins.forEach { it.onCycle() }
    }

    fun onPacketReceived() {

    }
}
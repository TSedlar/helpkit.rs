package rs.helpkit.api.util

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object Time {

    /**
     * Sleeps the current thread for the given milliseconds
     *
     * @param millis The milliseconds to sleep for
     */
    fun sleep(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (ignored: InterruptedException) {
        }
    }
}

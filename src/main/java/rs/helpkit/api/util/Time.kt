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

    /**
     * Gets the system's nano time and converts it to milliseconds.
     *
     * @return The system's millisecond equivalent of its nanoseconds.
     */
    fun now(): Long = toMillis(System.nanoTime())

    /**
     * Gets the millisecond equivalent to the specified number of nanoseconds.
     *
     * @param nanos The nanoseconds.
     * @return The equivalent number of milliseconds.
     */
    fun toMillis(nanos: Long): Long = nanos / 1_000_000

    /**
     * Formats a length of time.
     *
     * @param millis The time to format.
     * @return The String of time formatted into days/hours/minutes/seconds.
     */
    fun format(millis: Long): String {
        val hours = Math.abs((millis / (1000 * 60 * 60) % 24).toInt())
        val minutes = Math.abs((millis / (1000 * 60) % 60).toInt())
        val seconds = Math.abs((millis / 1000).toInt() % 60)
        return "%02d:%02d:%02d".format(hours, minutes, seconds)
    }
}

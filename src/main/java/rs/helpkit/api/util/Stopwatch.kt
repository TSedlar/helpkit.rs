package rs.helpkit.api.util


/**
 * @author Tyler Sedlar
 * @since 12/16/2015
 */
class Stopwatch(private val period: Long) {

    private var end: Long = 0
    private var start: Long = 0
    private var totalPauseTime: Long = 0
    private var pauseTime: Long = -1

    init {
        this.start = Time.toMillis(System.nanoTime())
        this.end = start + period
    }

    fun elapsed(): Long {
        return current() - start - totalPauseTime
    }

    fun remaining(): Long {
        return if (running()) end - current() else 0
    }

    fun running(): Boolean {
        return end == 0L || current() < end
    }

    private fun current(): Long {
        return if (paused()) pauseTime else Time.toMillis(System.nanoTime())
    }

    fun reset() {
        end = Time.toMillis(System.nanoTime()) + period
    }

    fun setEndIn(ms: Long): Long {
        end = Time.toMillis(System.nanoTime()) + ms
        return end
    }

    fun pause() {
        pauseTime = Time.toMillis(System.nanoTime())
    }

    fun paused(): Boolean = pauseTime != -1L

    fun resume() {
        if (paused()) {
            val pause = Time.toMillis(System.nanoTime()) - pauseTime
            start += pause
            end += pause
            totalPauseTime += pause
        }
        pauseTime = -1
    }

    fun elapsedString(): String {
        return Time.format(elapsed())
    }

    fun remainingString(): String {
        return Time.format(remaining())
    }

    override fun toString(): String {
        return if (running()) {
            String.format("Stopwatch[running=true,remaining=%s,elapsed=%s]", elapsedString(), remainingString())
        } else {
            String.format("Stopwatch[running=false,remaining=%s,elapsed=%s]", elapsedString(), remainingString())
        }
    }
}
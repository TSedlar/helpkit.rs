package rs.helpkit.api.util;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class Time {

    /**
     * Sleeps the current thread for the given milliseconds
     *
     * @param millis The milliseconds to sleep for
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}

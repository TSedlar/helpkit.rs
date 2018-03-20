package rs.helpkit.api;

import rs.helpkit.api.util.Time;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public abstract class Plugin implements Runnable {

    public boolean enabled = true;

    public abstract boolean validate();

    public abstract int loop();

    @Override
    public final void run() {
        int delay = 0;
        do {
            if (!validate()) {
                delay = 1000;
            } else {
                Time.sleep(delay);
                try {
                    delay = loop();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        } while (delay >= 0);
    }
}

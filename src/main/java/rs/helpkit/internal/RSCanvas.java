package rs.helpkit.internal;

import rs.helpkit.api.raw.Fields;
import rs.helpkit.api.util.Time;
import rs.helpkit.util.fx.GraphicsState;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class RSCanvas extends Canvas {

    public static final Dimension GAME_SIZE = new Dimension(765, 503);

    public BufferedImage buffer, raw;

    private boolean normalized = false;
    private boolean hidden = false;

    private Canvas original;
    private boolean running = false;

    public final List<Consumer<Graphics2D>> consumers = new ArrayList<>();

    public RSCanvas(Canvas original) {
        this.original = original;
        setBounds(original.getBounds());
        raw = new BufferedImage(GAME_SIZE.width, GAME_SIZE.height, BufferedImage.TYPE_3BYTE_BGR);
        buffer = new BufferedImage(GAME_SIZE.width, GAME_SIZE.height, BufferedImage.TYPE_3BYTE_BGR);
    }

    public void normalize() {
        normalized = true;
    }

    public void hideGraphics() {
        hidden = true;
    }

    @Override
    public boolean hasFocus() {
        return true;
    }

    @Override
    public Graphics getGraphics() {
        Graphics g = original.getGraphics();
        if (hidden) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            return g;
        }
        if (normalized) {
            return g;
        }
        if (g != null && buffer != null && raw != null) {
            Graphics2D paint = buffer.createGraphics();
            paint.drawImage(raw, 0, 0, null);
            consumers.forEach(consumer -> {
                try {
                    GraphicsState state = new GraphicsState(paint);
                    consumer.accept(paint);
                    state.restore();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            });
            paint.dispose();
            g.drawImage(buffer, 0, 0, null);
            g.dispose();
            return raw.createGraphics();
        }
        return null;
    }

    @Override
    public int hashCode() {
        return original.hashCode();
    }

    @SuppressWarnings("deprecation") // Applet is deprecated in Java9
    public void startReplacementTask(Applet applet) {
        if (!running) {
            running = true;
            new Thread(() -> {
                while (running) {
                    try {
                        original = (Canvas) applet.getComponent(0);
                        setBounds(original.getBounds());
                        Object producer = Fields.get("Client#interfaceProducer");
                        Fields.set("ComponentProducer#component", this, producer);
                        Fields.set("GameEngine#canvas", this, applet);
                        Time.sleep(1000);
                    } catch (Exception e) {}
                }
            }).start();
        }
    }

    public void stopReplacementTask() {
        running = false;
    }
}

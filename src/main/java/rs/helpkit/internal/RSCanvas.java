package rs.helpkit.internal;

import rs.helpkit.util.fx.GraphicsState;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class RSCanvas extends Canvas {

    public static final Dimension GAME_SIZE = new Dimension(765, 503);

    public BufferedImage buffer, raw;

    private final Canvas original;
    private boolean normalized = false;
    private boolean hidden = false;

    public final List<Consumer<Graphics2D>> consumers = new ArrayList<>();

    public RSCanvas(Canvas original) {
        this.original = original;
        raw = new BufferedImage(GAME_SIZE.width, GAME_SIZE.height, BufferedImage.TYPE_3BYTE_BGR);
        buffer = new BufferedImage(GAME_SIZE.width, GAME_SIZE.height, BufferedImage.TYPE_3BYTE_BGR);
        for (MouseListener listener : original.getMouseListeners()) {
            original.removeMouseListener(listener);
            addMouseListener(listener);
        }
        for (MouseMotionListener listener : original.getMouseMotionListeners()) {
            original.removeMouseMotionListener(listener);
            addMouseMotionListener(listener);
        }
        for (MouseWheelListener listener : original.getMouseWheelListeners()) {
            original.removeMouseWheelListener(listener);
            addMouseWheelListener(listener);
        }
        for (KeyListener listener : original.getKeyListeners()) {
            original.removeKeyListener(listener);
            addKeyListener(listener);
        }
        for (FocusListener listener : original.getFocusListeners()) {
            original.removeFocusListener(listener);
            addFocusListener(listener);
        }
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
        Graphics g = super.getGraphics();
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
}

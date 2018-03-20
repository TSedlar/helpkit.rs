package rs.helpkit.util.fx;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * @author Tyler Sedlar
 * @since 1/20/2016
 */
public class GraphicsState {

    private final Graphics2D g;
    private final Color color;
    private final Color background;
    private final Shape clip;
    private final Composite composite;
    private final Font font;
    private final Paint paint;
    private final RenderingHints hints;
    private final Stroke stroke;
    private final AffineTransform transform;

    public GraphicsState(Graphics2D g) {
        this.g = g;
        this.color = g.getColor();
        this.background = g.getBackground();
        this.clip = g.getClip();
        this.composite = g.getComposite();
        this.font = g.getFont();
        this.paint = g.getPaint();
        this.hints = g.getRenderingHints();
        this.stroke = g.getStroke();
        this.transform = g.getTransform();
    }

    public void restore() {
        g.setColor(color);
        g.setBackground(background);
        g.setClip(clip);
        g.setComposite(composite);
        g.setFont(font);
        g.setPaint(paint);
        g.setRenderingHints(hints);
        g.setStroke(stroke);
        g.setTransform(transform);
    }
}

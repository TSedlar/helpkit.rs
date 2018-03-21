package rs.helpkit.util.fx

import java.awt.*
import java.awt.geom.AffineTransform

/**
 * @author Tyler Sedlar
 * @since 1/20/2016
 */
class GraphicsState(private val g: Graphics2D) {
    private val color: Color? = g.color
    private val background: Color? = g.background
    private val clip: Shape? = g.clip
    private val composite: Composite? = g.composite
    private val font: Font? = g.font
    private val paint: Paint? = g.paint
    private val hints: RenderingHints? = g.renderingHints
    private val stroke: Stroke? = g.stroke
    private val transform: AffineTransform? = g.transform

    fun restore() {
        g.color = color
        g.background = background
        g.clip = clip
        g.composite = composite
        g.font = font
        g.paint = paint
        g.setRenderingHints(hints)
        g.stroke = stroke
        g.transform = transform
    }
}

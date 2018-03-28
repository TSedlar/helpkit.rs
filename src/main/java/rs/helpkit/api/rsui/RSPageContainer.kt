package rs.helpkit.api.rsui

import rs.helpkit.util.fx.GraphicsState
import java.awt.Graphics2D

/**
 * @author Tyler Sedlar
 * @since 3/26/2018
 */
class RSPageContainer : RSContainer() {

    var page: Any? = null

    private val pages: MutableMap<Any, FXComponent> = HashMap()

    override fun add(child: FXComponent) {
        error("#addPage should be used instead, as this is a stateful opcode of container.")
    }

    fun addPage(condition: Any, target: FXComponent) {
        target.parent = this
        pages[condition] = target
    }

    fun current(): FXComponent? = pages[page]

    override fun render(g: Graphics2D) {
        if (visible) {
            val state = GraphicsState(g)
            render(g, x + xOff, y + yOff)
            state.restore()
            pages.values.forEach { it.hide() }
            if (page != null && page in pages) {
                val comp = pages[page!!]
                comp!!.show()
                if (comp is RSContainer) {
                    comp.x = x
                    comp.y = y
                    comp.xOff = xOff
                    comp.yOff = yOff
                }
                comp.render(g)
            }
        }
    }
}
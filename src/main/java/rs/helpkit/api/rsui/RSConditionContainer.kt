package rs.helpkit.api.rsui

import java.awt.Graphics2D

/**
 * @author Tyler Sedlar
 * @since 3/26/2018
 */
class RSConditionContainer(parent: RSWindow): RSContainer(parent) {

    var condition: Any? = null

    private val conditions: MutableMap<Any, FXComponent> = HashMap()

    override fun add(child: FXComponent) {
        error("#addCondition should be used instead, as this is a stateful type of container.")
    }

    fun addCondition(condition: Any, target: FXComponent) {
        conditions[condition] = target
    }

    override fun render(g: Graphics2D) {
        if (visible) {
            render(g, x + xOff, y + yOff)
            if (condition != null && condition in conditions) {
                val comp = conditions[condition!!]
                if (comp is FXChildComponent) {
                    comp.render(g, x + xOff, y + yOff)
                } else if (comp is RSContainer) {
                    comp.x = comp.parent.x
                    comp.y = comp.parent.y
                    comp.xOff = comp.parent.xOff
                    comp.yOff = comp.parent.yOff
                    comp.render(g)
                }
            }
        }
    }
}
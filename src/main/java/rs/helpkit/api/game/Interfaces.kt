package rs.helpkit.api.game

import rs.helpkit.api.game.wrapper.RTComponent
import rs.helpkit.api.game.wrapper.node.RSHashTable
import rs.helpkit.api.game.wrapper.node.RSTableIterator
import rs.helpkit.api.raw.Fields

object Interfaces {

    fun positionsX(): IntArray? = Fields.asIntArray("Client#interfaceXPositions")
    fun positionsY(): IntArray? = Fields.asIntArray("Client#interfaceYPositions")
    fun widths(): IntArray? = Fields.asIntArray("Client#interfaceWidths")
    fun heights(): IntArray? = Fields.asIntArray("Client#interfaceHeights")

    fun raw(): Array<*>? = Fields["Client#interfaces"] as Array<*>?

    fun subWindowIterator(): RSTableIterator {
        return RSTableIterator(RSHashTable(Fields["Client#subWindowTable"]))
    }

    fun componentAt(parent: Int, child: Int): RTComponent? {
        val children = childrenAt(parent)
        return children.firstOrNull { it.id() == child }
    }

    fun componentAt(parent: Int, child: Int, index: Int): RTComponent? {
        val component = componentAt(parent, child)
        return component?.children()?.get(index)
    }

    fun childrenAt(idx: Int): List<RTComponent> {
        val childList: MutableList<RTComponent> = ArrayList()
        if (validate(idx)) {
            val children: Array<*> = raw()!![idx] as Array<*>
            childList.addAll(children.mapIndexed { childIdx, child -> RTComponent(child, childIdx) })
        }
        return childList
    }

    fun findChild(parent: RTComponent, filter: (child: RTComponent) -> Boolean): RTComponent? {
        if (filter(parent)) {
            return parent
        }
        parent.children().forEach { child ->
            val match = findChild(child, filter)
            if (match != null) {
                return match
            }
        }
        return null
    }

    fun findChild(filter: (child: RTComponent) -> Boolean): RTComponent? {
        val raw = raw()
        if (raw != null) {
            for (i in 0..raw.size) {
                childrenAt(i).forEach { child ->
                    val match = findChild(child, filter)
                    if (match != null) {
                        return match
                    }
                }
            }
        }
        return null
    }

    fun findChild(parent: Int, filter: (child: RTComponent) -> Boolean): RTComponent? {
        childrenAt(parent).forEach { child ->
            val match = findChild(child, filter)
            if (match != null) {
                return match
            }
        }
        return null
    }

    fun validate(parent: Int): Boolean {
        val widgets = raw()
        return widgets != null && parent >= 0 && parent < widgets.size && widgets[parent] != null
    }
}
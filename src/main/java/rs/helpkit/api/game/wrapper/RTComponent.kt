package rs.helpkit.api.game.wrapper

import rs.helpkit.api.game.access.Interfaces
import rs.helpkit.api.raw.Methods
import rs.helpkit.api.raw.Wrapper
import java.awt.Point
import java.awt.Rectangle

class RTComponent(referent: Any?, var index: Int) : Wrapper("RTComponent", referent) {

    fun children(): List<RTComponent> {
        return asArray("cs2components")!!.mapIndexed { childIndex, child -> RTComponent(child, childIndex) }
    }

    fun text(): String? = asString("text")

    fun tooltip(): String? = asString("buttonAction")

    fun actions(): Array<String>? = asStringArray("actions")

    fun uid(): Int = rawId().ushr(16)

    fun rawId(): Int = asInt("id")

    fun id(): Int {
        val id = rawId()
        return if (index < 0) id and 0xFF else index
    }

    fun rawParentId(): Int = asInt("parentId")

    fun arrayIndex(): Int = asInt("arrayIndex")

    fun parentUID(): Int {
        val uid = rawParentId()
        if (uid == -1) {
            val tableIterator = Interfaces.subWindowIterator()
            val containerId = rawId() ushr 16
            val node = tableIterator.findByFilter({ node -> node.subWindowId() == containerId })
            if (node != null && node.validate()) {
                return node.uid().toInt()
            }
        }
        return uid
    }

    fun parentIndex(): Int = parentUID() shr 16

    fun parent(): RTComponent? {
        val uid = parentUID()
        if (uid == -1) {
            return null
        }
        val parent = uid shr 16
        val child = uid and 0xFFFF
        return Interfaces.componentAt(parent, child)
    }

    fun relativeX(): Int = asInt("relativeX")

    fun relativeY(): Int = asInt("relativeY")

    fun width(): Int = asInt("width")

    fun height(): Int = asInt("height")

    fun horizontalScrollbarPosition(): Int = asInt("horizontalScrollbarPosition")

    fun verticalScrollbarPosition(): Int = asInt("verticalScrollbarPosition")

    fun type(): Int = asInt("type")

    fun hidden(): Boolean = asBoolean("hidden")

    fun cycle(): Int = asInt("cycle")

    fun spriteId(): Int = asInt("spriteId")

    fun contentType(): Int = asInt("contentType")

    fun x(): Int {
        val positionsX = Interfaces.positionsX()
        val index = arrayIndex()
        val relX = relativeX()
        val parent = parent()
        var x = 0
        if (parent != null && parent.validate()) {
            x = parent.x() - horizontalScrollbarPosition()
        } else {
            if (index >= 0 && positionsX!![index] > 0) {
                var absX = positionsX[index]
                if (type() > 0) {
                    absX += relX
                }
                return absX
            }
        }
        return x + relX
    }

    fun y(): Int {
        val positionsY = Interfaces.positionsY()
        val index = arrayIndex()
        val relY = relativeY()
        val parent = parent()
        var y = 0
        if (parent != null && parent.validate()) {
            y = parent.y() - verticalScrollbarPosition()
        } else {
            if (index >= 0 && positionsY!![index] > 0) {
                var absY = positionsY[index]
                if (type() > 0) {
                    absY += relY
                }
                return absY
            }
        }
        return y + relY
    }

    fun bounds(): Rectangle {
        return Rectangle(x(), y(), width(), height())
    }

    fun contains(x: Int, y: Int): Boolean {
        return bounds().contains(x, y)
    }

    fun contains(p: Point): Boolean {
        return contains(p.x, p.y)
    }

    fun toggleClickEvent() {
        val id = rawId()
        val x = x() + (width() / 2)
        val y = y() + (height() / 2)
        val actions = actions()
        if (actions != null && !actions.isEmpty()) {
            val action = actions[0]
            Methods.invoke("Client#processAction", null, -1, id, 57, 1, action, "", x, y)
        }
    }
}
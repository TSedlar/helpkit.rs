package rs.helpkit.api.game

import rs.helpkit.api.game.wrapper.CustomMenuItem
import rs.helpkit.api.raw.Fields
import java.awt.Rectangle

/**
 * @author Tyler Sedlar
 * @since 3/22/2018
 */
object GameMenu {

    val VALID_CUSTOM_MENU_ITEMS: MutableMap<String, CustomMenuItem> = HashMap()

    fun visible(): Boolean = Fields.asBoolean("Client#menuOpen")

    fun x(): Int = Fields.asInt("Client#menuX")

    fun y(): Int = Fields.asInt("Client#menuY")

    fun width(): Int = Fields.asInt("Client#menuWidth")

    fun height(): Int = Fields.asInt("Client#menuHeight")

    var itemCount: Int
        get() = Fields.asInt("Client#menuSize")
        set(value) = Fields.setInt("Client#menuSize", value)

    fun opcodes(): IntArray? = Fields.asIntArray("Client#menuOpcodes")

    fun actions(): Array<String>? = Fields.asStringArray("Client#menuActions")

    fun targets(): Array<String>? = Fields.asStringArray("Client#menuTargets")

    fun arg0(): IntArray? = Fields.asIntArray("Client#menuArg0")

    fun arg1(): IntArray? = Fields.asIntArray("Client#menuArg1")

    fun arg2(): IntArray? = Fields.asIntArray("Client#menuArg2")

    fun bounds(): Rectangle = Rectangle(x(), y(), width(), height())

    fun boundsAt(idx: Int): Rectangle = Rectangle(x(), y() + 18 + (idx * 15), width(), 15)

    private fun prepareMenuStrings(array: Array<*>, idx: Int) {
        System.arraycopy(array, idx, array, idx + 1, array.size - idx - 1)
    }

    private fun prepareMenuInts(array: IntArray, idx: Int) {
        System.arraycopy(array, idx, array, idx + 1, array.size - idx - 1)
    }

    fun addMenuItems(filter: (actions: Array<String>, targets: Array<String>) -> Boolean,
                     insertion: Int, vararg items: CustomMenuItem) {
        if (GameMenu.visible()) {
            val size = GameMenu.itemCount
            val actions = GameMenu.actions()
            val targets = GameMenu.targets()
            val opcodes = GameMenu.opcodes()
            val arg0 = GameMenu.arg0()
            val arg1 = GameMenu.arg1()
            val arg2 = GameMenu.arg2()
            if (actions != null && targets != null && opcodes != null && arg0 != null && arg1 != null && arg2 != null) {
                if (filter(actions, targets)) {
                    var longestText = ""
                    var added = 0
                    for (item in items) {
                        if (item.text !in actions.slice(IntRange(0, size))) {
                            val cancelOpcode = opcodes[0]
                            val cancelArg0 = arg0[0]
                            val cancelArg1 = arg1[0]
                            val cancelArg2 = arg2[0]
                            val idx = (size - insertion)
                            prepareMenuStrings(actions, idx)
                            prepareMenuStrings(targets, idx)
                            prepareMenuInts(opcodes, idx)
                            prepareMenuInts(arg0, idx)
                            prepareMenuInts(arg1, idx)
                            prepareMenuInts(arg2, idx)
                            actions[idx] = item.text
                            targets[idx] = ""
                            opcodes[idx] = cancelOpcode
                            arg0[idx] = cancelArg0
                            arg1[idx] = cancelArg1
                            arg2[idx] = cancelArg2
                            if (item.text.length > longestText.length) {
                                longestText = item.text
                            }
                            Fields.set("Client#menuActions", actions, null)
                            Fields.set("Client#menuTargets", targets, null)
                            Fields.set("Client#menuOpcodes", opcodes, null)
                            Fields.set("Client#menuArg0", arg0, null)
                            Fields.set("Client#menuArg1", arg1, null)
                            Fields.set("Client#menuArg2", arg2, null)
                            GameMenu.itemCount++
                            VALID_CUSTOM_MENU_ITEMS[item.text] = item
                            added++
                        }
                    }
                    val width = (longestText.length * 8)
                    if (width > GameMenu.width()) {
                        Fields.setInt("Client#menuWidth", width, null)
                    }
                    Fields.setInt("Client#menuHeight", GameMenu.height() + (15 * added), null)
                    Fields.setInt("Client#menuY", GameMenu.y() - (15 * added), null)
                }
                items.forEachIndexed { idx, item ->
                    item.bounds = GameMenu.boundsAt(insertion + idx)
                }
            }
        } else {
            items.forEach { VALID_CUSTOM_MENU_ITEMS.remove(it.text) }
        }
    }
}
package rs.helpkit.api.game.access

import rs.helpkit.api.game.wrapper.CustomMenuItem
import rs.helpkit.api.raw.Fields
import java.awt.Rectangle
import java.util.*
import kotlin.collections.HashMap

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

    fun actions(): Array<String?>? = Fields.asStringArray("Client#menuActions")

    fun targets(): Array<String?>? = Fields.asStringArray("Client#menuTargets")

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

    fun addMenuItems(filter: (actions: Array<String?>, targets: Array<String?>) -> Boolean,
                     insertion: Int, vararg items: CustomMenuItem) {
        if (visible()) {
            val size = itemCount
            val actions = actions()
            val targets = targets()
            val opcodes = opcodes()
            val arg0 = arg0()
            val arg1 = arg1()
            val arg2 = arg2()
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
                            itemCount++
                            VALID_CUSTOM_MENU_ITEMS[item.text] = item
                            added++
                        }
                    }
                    val width = (longestText.length * 8)
                    if (width > width()) {
                        Fields.setInt("Client#menuWidth", width, null)
                    }
                    Fields.setInt("Client#menuHeight", height() + (15 * added), null)
                    Fields.setInt("Client#menuY", y() - (15 * added), null)
                }
                items.forEachIndexed { idx, item ->
                    item.bounds = boundsAt(insertion + idx)
                }
            }
        } else {
            items.forEach { VALID_CUSTOM_MENU_ITEMS.remove(it.text) }
        }
    }

    fun sort(vararg priority: String) {
        return sort(priority.reversedArray().mapIndexed { idx, item -> item to idx + 1 }.toMap())
    }

    fun sort(weights: Map<String, Int>) {
        val size = GameMenu.itemCount
        val actions = GameMenu.actions()!!.clone()
        val targets = GameMenu.targets()!!.clone()
        val opcodes = GameMenu.opcodes()!!.clone()
        val arg0 = GameMenu.arg0()!!.clone()
        val arg1 = GameMenu.arg1()!!.clone()
        val arg2 = GameMenu.arg2()!!.clone()

        val items = Array(size) { i ->
            Action(actions[i]!!, targets[i]!!, opcodes[i], arg0[i], arg1[i], arg2[i])
        }

        // Modify
        Arrays.sort(items, { a, b -> compare(a, b, weights) })

        // Write them back
        for (i in 0 until size) {
            val item = items[i]
            actions[i] = item.action
            targets[i] = item.target
            opcodes[i] = item.opcode
            arg0[i] = item.arg0
            arg1[i] = item.arg1
            arg2[i] = item.arg2
        }

        Fields.set("Client#menuActions", actions)
        Fields.set("Client#menuTargets", targets)
        Fields.set("Client#menuOpcodes", opcodes)
        Fields.set("Client#menuArg0", arg0)
        Fields.set("Client#menuArg1", arg1)
        Fields.set("Client#menuArg2", arg2)
    }

    private val TAG_REGEX = "<[^>]*>".toRegex()

    private fun stripTarget(target: String): String {
        return target.replace(TAG_REGEX,"")
    }

    private fun weightOf(action: Action, weights: Map<String, Int>): Int {
        return weights.entries.find {
            return@find (action.action + " " + stripTarget(action.target)).startsWith(it.key)
        }?.value ?: 0
    }

    private fun compare(a: Action, b: Action, weights: Map<String, Int>): Int {
        return Integer.compare(weightOf(a, weights), weightOf(b, weights))
    }
}


private class Action(var action: String, var target: String, var opcode: Int, var arg0: Int, var arg1: Int, var arg2: Int)
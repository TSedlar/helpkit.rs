package rs.helpkit.api.game

import rs.helpkit.api.raw.Fields
import java.awt.Rectangle

/**
 * @author Tyler Sedlar
 * @since 3/22/2018
 */
object GameMenu {

    fun visible(): Boolean = Fields.asBoolean("Client#menuOpen")

    fun x(): Int = Fields.asInt("Client#menuX")

    fun y(): Int = Fields.asInt("Client#menuY")

    fun width(): Int = Fields.asInt("Client#menuWidth")

    fun height(): Int = Fields.asInt("Client#menuHeight")

    fun itemCount(): Int = Fields.asInt("Client#menuSize")

    fun opcodes(): IntArray? = Fields.asIntArray("Client#menuOpcodes")

    fun actions(): Array<String>? = Fields.asStringArray("Client#menuActions")

    fun targets(): Array<String>? = Fields.asStringArray("Client#menuTargets")

    fun arg0(): IntArray? = Fields.asIntArray("Client#menuArg0")

    fun arg1(): IntArray? = Fields.asIntArray("Client#menuArg1")

    fun arg2(): IntArray? = Fields.asIntArray("Client#menuArg2")

    fun bounds(): Rectangle = Rectangle(x(), y(), width(), height())
}
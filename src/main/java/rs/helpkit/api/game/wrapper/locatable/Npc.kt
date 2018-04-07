package rs.helpkit.api.game.wrapper.locatable

import rs.helpkit.api.raw.Fields

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
class Npc(referent: Any?) : RSCharacter("Npc", referent) {

    private fun composite(): Any? = get("composite")

    fun id(): Int = Fields.asInt("NpcDefinition#id", composite())

    fun name(): String? = Fields.asString("NpcDefinition#name", composite())
}

package rs.helpkit.api.game

import rs.helpkit.api.game.wrapper.RTComponent

/**
 * @author Tyler Sedlar
 * @since 3/22/2018
 */
enum class GameTab {

    CLAN_CHAT(),
    FRIENDS_LIST(),
    IGNORE_LIST(),
    LOGOUT(),
    OPTIONS(),
    EMOTES(),
    MUSIC_PLAYER(),
    COMBAT_OPTIONS(),
    STATS(),
    QUEST_LIST("Quest List", "Achievement Diaries", "Minigames"),
    INVENTORY(),
    WORN_EQUIPMENT(),
    PRAYER(),
    MAGIC();

    private val tooltips: Array<out String>

    constructor(vararg tooltips: String) {
        this.tooltips = tooltips
    }

    constructor() {
        val name = this.toString().replace("_", " ").toLowerCase()
        val splits = name.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var label = ""
        for (split in splits) {
            if (!label.isEmpty()) {
                label += " "
            }
            label += split.substring(0, 1).toUpperCase() + split.substring(1)
        }
        this.tooltips = arrayOf(label)
    }

    fun component(): RTComponent? {
        return Interfaces.findChild { child ->
            if (child.hidden() || -Math.abs(child.cycle()) + Math.abs(Client.cycle()) > 5) {
                return@findChild false
            }
            val actions = child.actions()
            if (actions != null && actions.isNotEmpty()) {
                return@findChild actions[0] in tooltips
            }
            return@findChild false
        }
    }

    fun viewing(): Boolean {
        val component = component()
        return component != null && !component.hidden() && component.spriteId() != -1
    }

    companion object {
        fun current(): GameTab? {
            return GameTab.values().firstOrNull { it.viewing() }
        }
    }
}
package rs.helpkit.plugins.tab

import rs.helpkit.api.game.access.GameMenu
import rs.helpkit.api.game.access.GameTab
import rs.helpkit.api.game.wrapper.CustomMenuItem
import rs.helpkit.api.rsui.RSContainer
import rs.helpkit.api.util.Renderable
import rs.helpkit.plugins.PluginTab
import rs.helpkit.util.io.Resources

abstract class CustomTab(private val container: PluginTab, val name: String, iconPath: String) : Renderable {

    val icon = Resources.img(iconPath)

    private val menuItem: CustomMenuItem = CustomMenuItem(name, {
        container.customTab = this
        GameTab.OPTIONS.component()?.toggleClickEvent()
    })

    abstract fun panel(): RSContainer

    companion object {
        fun updateMenuItems(items: List<CustomTab>) {
            GameMenu.addMenuItems({ actions, _ ->
                actions[GameMenu.itemCount - 1] == "Options"
            }, 1, *items.map { it.menuItem }.toTypedArray())
        }
    }
}
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

    private var panel: RSContainer? = null

    private val menuItem: CustomMenuItem = CustomMenuItem(name, {
        container.customPanel?.hide()
        container.customTab = this
        container.customPanel = panel
        container.customPanel!!.show()
        if (GameTab.current() != GameTab.OPTIONS) {
            GameTab.OPTIONS.component()?.toggleClickEvent()
        }
    })

    abstract fun startup()
    abstract fun panel(): RSContainer

    private fun run() {
        if (panel == null) {
            panel = panel()
            startup()
        }
    }

    companion object {
        fun process(items: List<CustomTab>) {
            items.forEach { it.run() }
            GameMenu.addMenuItems({ actions, _ ->
                actions[GameMenu.itemCount - 1] == "Options"
            }, 1, *items.map { it.menuItem }.toTypedArray())
        }
    }
}
package rs.helpkit.plugins.tab

import rs.helpkit.api.rsui.*
import rs.helpkit.plugins.PluginTab

/**
 * @author Tyler Sedlar
 * @since 3/26/2018
 */
class XPTrackerTab(container: PluginTab) : CustomTab(
        container, "XP Tracker", "/images/xp-tracker-tab.png"
) {

    private enum class State {
        HOME, ADD
    }

    override fun panel(): RSTabContentPanel {
        val panel = RSTabContentPanel()
        val container = RSConditionContainer(panel)

        val home = RSContainer(panel)
        home.add(RSButton(46, 218, 100, 20)
                .bindTo { "Add Tracker" }
                .onClick { _, _ -> container.condition = State.ADD })
        container.addCondition(State.HOME, home)

        val data = RSContainer(panel)
        data.add(RSUI.closeButton(163, 5)
                .onClick { _, _ -> container.condition = State.HOME })
        container.addCondition(State.ADD, data)

        container.condition = State.HOME
        panel.add(container)
        return panel
    }
}
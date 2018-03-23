package rs.helpkit.plugins.tab

import rs.helpkit.api.rsui.RSTabContentPanel
import rs.helpkit.plugins.PluginTab

class TimersTab(container: PluginTab) : CustomTab(
        container, "Timers", "/images/timers-tab.png"
) {

    override fun panel(): RSTabContentPanel {
        return RSTabContentPanel()
    }
}
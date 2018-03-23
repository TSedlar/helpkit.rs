package rs.helpkit.plugins.tab

import rs.helpkit.api.rsui.RSTabContentPanel
import rs.helpkit.plugins.PluginTab

class XPTrackerTab(container: PluginTab) : CustomTab(
        container, "XP Tracker", "/images/xp-tracker-tab.png"
) {

    override fun panel(): RSTabContentPanel {
        return RSTabContentPanel()
    }
}
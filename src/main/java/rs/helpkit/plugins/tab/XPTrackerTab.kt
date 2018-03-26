package rs.helpkit.plugins.tab

import rs.helpkit.api.rsui.RSButton
import rs.helpkit.api.rsui.RSTabContentPanel
import rs.helpkit.plugins.PluginTab

class XPTrackerTab(container: PluginTab) : CustomTab(
        container, "XP Tracker", "/images/xp-tracker-tab.png"
) {

    override fun panel(): RSTabContentPanel {
        val panel = RSTabContentPanel()
        panel.add(RSButton(46, 218, 100, 20)
                .bindTo { "Add Tracker" })
        return panel
    }
}
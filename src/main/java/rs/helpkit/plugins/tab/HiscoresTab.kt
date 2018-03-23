package rs.helpkit.plugins.tab

import rs.helpkit.api.rsui.RSTabContentPanel
import rs.helpkit.plugins.PluginTab

class HiscoresTab(container: PluginTab) : CustomTab(
        container, "Hiscores", "/images/hiscores-tab.png"
) {

    override fun panel(): RSTabContentPanel {
        return RSTabContentPanel()
    }
}
package rs.helpkit.plugins.tab

import rs.helpkit.api.rsui.RSTabContentPanel
import rs.helpkit.plugins.PluginTab

class GrandExchangeTab(container: PluginTab) : CustomTab(
        container, "Grand Exchange", "/images/ge-tab.png"
) {

    override fun panel(): RSTabContentPanel {
        return RSTabContentPanel()
    }
}
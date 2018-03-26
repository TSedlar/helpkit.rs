package rs.helpkit.plugins.tab

import rs.helpkit.api.rsui.RSContainer
import rs.helpkit.plugins.PluginTab
import java.awt.Graphics2D

class GrandExchangeTab(container: PluginTab) : CustomTab(
        container, "Grand Exchange", "/images/ge-tab.png"
) {

    override fun panel(): RSContainer {
        return RSContainer()
    }

    override fun render(g: Graphics2D) {
    }
}
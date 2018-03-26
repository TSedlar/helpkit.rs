package rs.helpkit.plugins.tab

import rs.helpkit.api.rsui.RSContainer
import rs.helpkit.plugins.PluginTab
import java.awt.Graphics2D

class HiscoresTab(container: PluginTab) : CustomTab(
        container, "Hiscores", "/images/hiscores-tab.png"
) {

    override fun panel(): RSContainer {
        return RSContainer()
    }

    override fun render(g: Graphics2D) {
    }
}
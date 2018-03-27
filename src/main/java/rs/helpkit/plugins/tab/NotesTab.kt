package rs.helpkit.plugins.tab

import rs.helpkit.api.rsui.RSContainer
import rs.helpkit.plugins.PluginTab
import java.awt.Graphics2D

class NotesTab(container: PluginTab) : CustomTab(
        container, "Notes", "/images/notes-tab.png"
) {

    override fun startup() {
    }

    override fun panel(): RSContainer {
        return RSContainer()
    }

    override fun render(g: Graphics2D) {
    }
}
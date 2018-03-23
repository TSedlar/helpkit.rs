package rs.helpkit.plugins.tab

import rs.helpkit.api.rsui.RSTabContentPanel
import rs.helpkit.plugins.PluginTab

class NotesTab(container: PluginTab) : CustomTab(
        container, "Notes", "/images/notes-tab.png"
) {

    override fun panel(): RSTabContentPanel {
        return RSTabContentPanel()
    }
}
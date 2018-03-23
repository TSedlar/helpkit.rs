package rs.helpkit.plugins

import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.GameMenu
import rs.helpkit.api.game.GameTab
import rs.helpkit.api.game.Interfaces
import rs.helpkit.api.raw.Fields
import rs.helpkit.api.rsui.RSWindow
import rs.helpkit.api.util.Renderable
import rs.helpkit.api.util.Schedule
import rs.helpkit.util.io.Resources
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.image.BufferedImage

@Manifest(
        author = "Static",
        name = "PluginTab",
        description = "The plugin that manages every custom plugin that uses the plugin tab",
        version = 1.0
)
class PluginTab : Plugin(), Renderable {

    var tabImage: BufferedImage = Resources.img("/images/ui/tab.png")
    var tabSelectedImage: BufferedImage = Resources.img("/images/ui/tab-selected.png")

    var bounds: Rectangle? = null
    var contentBounds: Rectangle? = null

    var viewing: Boolean = false

    var customWindow: RSWindow? = null

    override fun validate(): Boolean {
        return true
    }

    fun addMenuItem(text: String) {
        if (GameMenu.visible()) {
            val size = GameMenu.itemCount
            val actions = GameMenu.actions()
            val targets = GameMenu.targets()
            val opcodes = GameMenu.opcodes()
            val arg0 = GameMenu.arg0()
            val arg1 = GameMenu.arg1()
            val arg2 = GameMenu.arg2()
            if (actions != null && targets != null && opcodes != null && arg0 != null && arg1 != null && arg2 != null) {
                val cancelOpcode = opcodes[0]
                val cancelArg0 = arg0[0]
                val cancelArg1 = arg1[0]
                val cancelArg2 = arg2[0]
                // shift array to the right..
                actions[0] = text
                targets[0] = ""
                opcodes[0] = cancelOpcode
                arg0[0] = cancelArg0
                arg1[0] = cancelArg1
                arg2[0] = cancelArg2
                Fields.set("Client#menuActions", actions, null)
                Fields.set("Client#menuTargets", targets, null)
                Fields.set("Client#menuOpcodes", opcodes, null)
                Fields.set("Client#menuArg0", arg0, null)
                Fields.set("Client#menuArg1", arg1, null)
                Fields.set("Client#menuArg2", arg2, null)
                GameMenu.itemCount--
//                Fields.set("Client#menuSize", size + 1, null) // borks
            }
        }
    }

    @Schedule(100)
    fun testInterfaces() {
        val options = GameTab.OPTIONS.component()
        bounds = options?.bounds()
        viewing = GameTab.current() == GameTab.OPTIONS
        addMenuItem("LOL!")
        val contents = Interfaces.findChild { child ->
            val text = child.text()
            return@findChild text != null && text.contains("Advanced options")
        }?.parent()?.parent()?.parent()?.parent()
        if (contents != null) {
            val arrIdx = contents.arrayIndex()
            if (arrIdx != -1) {
                Fields.set("RTComponent#hidden", customWindow != null, contents.get())
            }
        }
        contentBounds = contents?.bounds()
    }

    override fun render(g: Graphics2D) {
        g.color = Color.GREEN
        if (bounds != null && customWindow != null) {
            if (viewing) {
                g.drawImage(tabSelectedImage, bounds!!.x, bounds!!.y, null)
            } else {
                g.drawImage(tabImage, bounds!!.x, bounds!!.y, null)
            }
            if (customWindow != null) {
                customWindow!!.render(g)
            }
        }
    }
}
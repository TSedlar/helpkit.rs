package rs.helpkit.plugins.tab

import rs.helpkit.api.game.access.Skills
import rs.helpkit.api.rsui.*
import rs.helpkit.plugins.PluginTab
import rs.helpkit.util.fx.Images
import rs.helpkit.util.io.Resources
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.RescaleOp
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Tyler Sedlar
 * @since 3/26/2018
 */
class XPTrackerTab(container: PluginTab) : CustomTab(
        container, "XP Tracker", "/images/xp-tracker-tab.png"
) {

    private enum class Page {
        HOME, ADD
    }

    private val trackers: MutableMap<Skills, FXComponent> = HashMap()
    private val popouts: MutableMap<Skills, RSFrame> = ConcurrentHashMap()

    private var home: RSContainer? = null
    private var data: RSContainer? = null

    private fun addHomePanel(container: RSPageContainer) {
        home = RSContainer()
        home!!.add(RSButton(46, 218, 100, 20)
                .bindTo { "Add Tracker" }
                .onClick { _, _ -> container.page = Page.ADD })
        container.addPage(Page.HOME, home!!)
    }

    private fun addTrackerUI(container: RSContainer, skill: Skills, x: Int, y: Int) {
        container.add(RSImage(Skills.ICONS[skill.index()], null, x, y))
        container.add(RSProgressBar(x + 23, y + 8, 103, 10, {
            50
        }))
        container.add(RSLabel("84.2k/hr", x + 22, y - 4)
                .useFont(Resources.FONT_RS_SMALL, 16)
                .color(Color(255, 184, 63)))
        container.add(RSLabel("04:12:36", x + 127, y - 4)
                .useFont(Resources.FONT_RS_SMALL, 16)
                .color(Color(255, 184, 63))
                .rightAlign())
        container.add(RSLabel("92", x + 128, y + 7)
                .useFont(Resources.FONT_RS_SMALL, 16)
                .color(Color.WHITE))
    }

    private fun addTrackerPanel(skill: Skills) {
        home?.let {
            if (skill !in trackers) {
                val container = RSContainer()
                container.add(RSOctoCheck(10, 12 + (32 * trackers.size))
                        .onValueChange {
                            if (!it) {
                                popouts.remove(skill)
                            } else {
                                popouts[skill] = createTrackerPopout(skill)
                            }
                        })
                addTrackerUI(container, skill, 36, 14 + (32 * trackers.size))
                it.add(container)
                trackers[skill] = container
            }
        }
    }

    private fun createTrackerPopout(skill: Skills): RSFrame {
        val frame = RSFrame(164, 49)
        frame.x = 150
        frame.y = 150
        val container = RSContainer()
        addTrackerUI(container, skill, 10, 18)
        frame.add(container)
        return frame
    }

    private fun addDataPanel(container: RSPageContainer) {
        data = RSContainer()
        data!!.add(RSUI.closeButton(163, 5)
                .onClick { _, _ -> container.page = Page.HOME })
        val startY = 35
        var placementX = 22
        var placementY = startY
        Skills.runescapeOrder().forEachIndexed { idx, skill ->
            val icon = Skills.ICONS[skill.index()]
            val hoveredIcon = RescaleOp(0.5F, 0F, null)
                    .filter(Images.toIndexedImage(icon), null)
            data!!.add(RSImage(icon, hoveredIcon, placementX, placementY)
                    .onClick { _, _ ->
                        addTrackerPanel(skill)
                        container.page = Page.HOME
                    })
            placementY += 28
            if ((idx + 1) % 8 == 0) {
                placementX += 62
                placementY = startY
            }
        }
        container.addPage(Page.ADD, data!!)
    }

    override fun panel(): RSContainer {
        val container = RSPageContainer()

        addHomePanel(container)
        addDataPanel(container)

        container.page = Page.HOME
        return container
    }

    override fun render(g: Graphics2D) {
        popouts.values.forEach { it.render(g) }
    }
}
package rs.helpkit.plugins.tab

import rs.helpkit.api.game.access.Chatbox
import rs.helpkit.api.game.access.Client
import rs.helpkit.api.game.access.Skills
import rs.helpkit.api.rsui.*
import rs.helpkit.api.util.Time
import rs.helpkit.plugins.PluginTab
import rs.helpkit.pref.RSPreferences
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
class XPTrackerTab(var container: PluginTab) : CustomTab(
        container, "XP Tracker", "/images/xp-tracker-tab.png"
) {

    private enum class Page {
        HOME, ADD
    }

    private val MAX_TRACKERS = 6

    private val trackers: MutableMap<Skills, FXComponent> = ConcurrentHashMap()
    private val popouts: MutableMap<Skills, RSFrame> = ConcurrentHashMap()
    private val starts: MutableMap<Skills, Pair<Long, Int>> = ConcurrentHashMap()

    private var home: RSContainer? = null
    private var data: RSContainer? = null

    private var deleting = false

    override fun startup() {
    }

    private fun addHomePanel(container: RSPageContainer) {
        home = RSContainer()
        home!!.add(RSButton(14, 223, 100, 25)
                .bindTo { "Add Tracker" }
                .onClick { _, _ ->
                    if (trackers.size == MAX_TRACKERS) {
                        Chatbox.sendMessage("You can only have $MAX_TRACKERS trackers", Chatbox.COLOR_WARN)
                    } else {
                        container.page = Page.ADD
                    }
                })
        home!!.add(RSButton(120, 223, 25, 25)
                .bindImage(RSUI.REFRESH_ALL)
                .onClick { _, _ ->
                    starts.keys.forEach { skill ->
                        starts[skill] = Pair(Time.now(), skill.experience())
                    }
                })
        val delete = RSButton(151, 223, 25, 25)
        home!!.add(delete
                .bindImage(RSUI.TRASH)
                .onClick { _, _ ->
                    deleting = !deleting
                    if (deleting) {
                        delete.background = Color(255, 0, 0, 130)
                    } else {
                        delete.background = RSButton.DEFAULT_BACKGROUND

                        val skills: MutableList<Skills> = ArrayList()

                        trackers.forEach { key, comp ->
                            comp.hide()
                            home!!.children.remove(comp)
                            trackers.remove(key)
                            skills.add(key)
                        }

                        skills.forEach { addTrackerPanel(it) }
                    }
                })
        container.addPage(Page.HOME, home!!)
    }

    private fun addTrackerUI(container: RSContainer, skill: Skills, x: Int, y: Int) {
        container.add(RSImage(Skills.ICONS[skill.index()], null, x, y))

        container.add(RSProgressBar(x + 23, y + 8, 103, 10, {
            skill.percentToNext()
        }))

        var lastHourly = 0L
        var lastHourlyNumber = 0
        var startedTraining = false

        container.add(RSLabel(x + 22, y - 4)
                .bindTo {
                    if (skill in starts) {
                        val elapsed = (Time.now() - starts[skill]!!.first)
                        val gained = (skill.experience() - starts[skill]!!.second)
                        if (gained > 0 && !startedTraining) {
                            starts[skill] = Pair(Time.now(), skill.experience())
                            startedTraining = true
                        }
                        if (lastHourly == 0L || Time.now() - lastHourly >= 2500) {
                            lastHourly = Time.now()
                            lastHourlyNumber = Skills.hourlyExperience(elapsed, gained)
                        }
                        return@bindTo "$lastHourlyNumber/hr"
                    } else {
                        return@bindTo "0/hr"
                    }
                }
                .bindState {
                    if (!Client.loggedIn()) {
                        starts.remove(skill)
                        startedTraining = false
                    } else if (skill !in starts) {
                        starts[skill] = Pair(Time.now(), skill.experience())
                    }
                }
                .useFont(Resources.FONT_RS_SMALL, 16)
                .color(Color(255, 184, 63)))

        container.add(RSLabel(x + 127, y - 4)
                .bindTo {
                    if (skill !in starts || skill.experience() - starts[skill]!!.second == 0) {
                        "Unknown TTL"
                    } else {
                        Skills.timeToLevel(skill.remainingToNext(), lastHourlyNumber)
                    }
                }
                .useFont(Resources.FONT_RS_SMALL, 16)
                .color(Color(255, 184, 63))
                .rightAlign())

        container.add(RSLabel(x + 128, y + 8)
                .bindTo { (skill.realLevel() + 1).toString() }
                .useFont(Resources.FONT_RS_SMALL, 16)
                .color(Color.WHITE))

        container.add(RSImage(RSUI.REFRESH_ONE, null, x + 130, y - 3)
                .onClick { _, _ ->
                    if (deleting) {
                        RSPreferences.setSkillEnabled(skill, false)
                        trackers[skill]?.hide()
                        home!!.children.remove(trackers[skill])
                        trackers.remove(skill)
                        popouts.remove(skill)
                        starts.remove(skill)
                    } else {
                        starts[skill] = Pair(Time.now(), skill.experience())
                    }
                }
                .disableHover()
                .bindState { self ->
                    self.image = if (deleting) RSUI.DELETE_ONE else RSUI.REFRESH_ONE
                })
    }

    private fun addTrackerPanel(skill: Skills) {
        if (trackers.size == MAX_TRACKERS) {
            return
        }
        RSPreferences.setSkillEnabled(skill, true)
        home?.let {
            if (skill !in trackers) {
                val container = RSContainer()
                container.add(RSOctoCheck(10, 12 + (32 * trackers.size))
                        .onValueChange {
                            if (!it) {
                                RSPreferences.setSkillPopped(skill, false)
                                popouts.remove(skill)
                            } else {
                                RSPreferences.setSkillPopped(skill, true)
                                popouts[skill] = createTrackerPopout(skill)
                            }
                        }
                        .bindState { self -> self.selected = skill in popouts })
                if (skill !in starts) {
                    starts[skill] = Pair(Time.now(), skill.experience())
                }
                addTrackerUI(container, skill, 36, 14 + (32 * trackers.size))
                it.add(container)
                trackers[skill] = container
            }
        }
    }

    private fun createTrackerPopout(skill: Skills): RSFrame {
        val frame = RSFrame(156, 39)
        frame.x = 150
        frame.y = 150
        val loc = RSPreferences.skillLocationFor(skill)
        if (loc.x != -1 && loc.y != -1) {
            frame.x = loc.x
            frame.y = loc.y
        }
        val container = RSContainer()
        addTrackerUI(container, skill, 7, 12)
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

        Skills.values().forEach {
            if (RSPreferences.isSkillEnabled(it)) {
                addTrackerPanel(it)
                if (RSPreferences.isSkillPopout(it)) {
                    popouts[it] = createTrackerPopout(it)
                }
            }
        }

        container.page = Page.HOME
        return container
    }

    override fun render(g: Graphics2D) {
        popouts.forEach { skill, frame ->
            frame.render(g)
            val bounds = frame.exactBounds()
            RSPreferences.setSkillLocation(skill, bounds.x, bounds.y)
        }
    }
}
package rs.helpkit.api.game.access

import rs.helpkit.api.util.Stopwatch
import rs.helpkit.api.util.Time
import rs.helpkit.util.io.Resources

enum class Skills private constructor(private val cooldown: Int) {

    ATTACK(-1),
    DEFENCE(-1),
    STRENGTH(-1),
    HITPOINTS(-1),
    RANGED(-1),
    PRAYER(-1),
    MAGIC(-1),
    COOKING(3000),
    WOODCUTTING(3000),
    FLETCHING(3000),
    FISHING(3000),
    FIREMAKING(-1),
    CRAFTING(3000),
    SMITHING(3000),
    MINING(-1),
    HERBLORE(3000),
    AGILITY(-1),
    THIEVING(-1),
    SLAYER(-1),
    FARMING(-1),
    RUNECRAFT(-1),
    HUNTER(-1),
    CONSTRUCTION(-1);

    private var idle: Int = 0
    private var alerted: Long = 0
    private var lastTrained: Long = 0

    init {
        this.lastTrained = -1
    }

    fun cooldown(): Int {
        return cooldown
    }

    fun setLastTrained(lastTrained: Long) {
        this.lastTrained = lastTrained
    }

    fun lastTrained(): Long {
        return lastTrained
    }

    fun alerted(): Long {
        return alerted
    }

    fun setAlerted(alerted: Long) {
        this.alerted = alerted
    }

    fun resetIdle() {
        idle = 0
    }

    fun addIdle(idle: Int) {
        this.idle += idle
    }

    fun idle(): Int {
        return idle
    }

    fun index(): Int {
        return ordinal
    }

    fun runescapeIndex(): Int {
        for (i in 0 until Skills.values().size) {
            if (runescapeOrder()[i] == this) {
                return i
            }
        }
        return -1
    }

    fun currentLevel(): Int {
        return try {
            Client.levels()!![ordinal]
        } catch (e: Exception) {
            -1
        }

    }

    fun realLevel(): Int {
        return try {
            Client.baseLevels()!![ordinal]
        } catch (e: Exception) {
            currentLevel()
        }

    }

    fun experience(): Int {
        return try {
            Client.experiences()!![ordinal]
        } catch (e: Exception) {
            -1
        }

    }

    fun remainingExperienceTo(target: Int): Int {
        return if (target <= 99) experienceAt(target) - experience() else 0
    }

    fun remainingToNext(): Int {
        return remainingExperienceTo(realLevel() + 1)
    }

    fun percentToNext(): Int {
        if (realLevel() <= 98) {
            val total = (experienceAt(realLevel() + 1) - experienceAt(realLevel())).toDouble()
            val complete = (experience() - experienceAt(realLevel())).toDouble()
            return (complete / total * 100.0).toInt()
        }
        return 100
    }

    fun floatPercent(): Double {
        if (realLevel() <= 98) {
            val total = (experienceAt(realLevel() + 1) - experienceAt(realLevel())).toDouble()
            val complete = (experience() - experienceAt(realLevel())).toDouble()
            return complete / total * 100.0
        }
        return 100.0
    }

    fun skillName(): String {
        return name[0] + name.substring(1).toLowerCase()
    }

    fun iconPath(): String {
        return "/images/skill/${skillName().toLowerCase()}.gif"
    }

    companion object {

        val EXPERIENCE_TABLE = intArrayOf(0, 0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 1358, 1584, 1833, 2107,
                2411, 2746, 3115, 3523, 3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363,
                14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408, 33648, 37224, 41171, 45529, 50339, 55649,
                61512, 67983, 75127, 83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040, 203254,
                224466, 247886, 273742, 302288, 333804, 368599, 407015, 449428, 496254, 547953, 605032, 668051,
                737627, 814445, 899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200, 1798808, 1986068,
                2192818, 2421087, 2673114, 2951373, 3258594, 3597792, 3972294, 4385776, 4842295, 5346332, 5902831,
                6517253, 7195629, 7944614, 8771558, 9684577, 10692629, 11805606, 13034431)

        val ICONS = Skills.values().map { Resources.img(it.iconPath()) }

        fun skillFor(name: String): Skills? {
            for (skill in Skills.values()) {
                if (skill.name.equals(name, ignoreCase = true)) {
                    return skill
                }
            }
            return null
        }

        fun skillAt(index: Int): Skills? {
            return if (index >= 0 && index < Skills.values().size) Skills.values()[index] else null
        }

        fun experienceAt(level: Int): Int {
            return EXPERIENCE_TABLE[level]
        }

        fun levelAt(exp: Int): Int {
            for (i in EXPERIENCE_TABLE.indices) {
                if (exp < EXPERIENCE_TABLE[i]) {
                    return i - 1
                }
            }
            return -1
        }

        fun totalExperience(): Long {
            var sum: Long = 0
            for (skill in Skills.values()) {
                sum += skill.experience().toLong()
            }
            return sum
        }

        fun totalLevel(): Int {
            var sum = 0
            for (skill in Skills.values()) {
                sum += skill.realLevel()
            }
            return sum
        }

        fun hourlyExperience(elapsed: Long, experienceGained: Int): Int {
            return if (elapsed > 0) (experienceGained * 3600000.0 / elapsed).toInt() else 0
        }

        fun hourlyExperience(timer: Stopwatch, experienceGained: Int): Int {
            return if (timer.elapsed() > 0) (experienceGained * 3600000.0 / timer.elapsed()).toInt() else 0
        }

        fun timeToLevel(expToNext: Int, hourlyExp: Int): String {
            return Time.format((expToNext.toDouble() * 3600000.0 / hourlyExp.toDouble()).toLong())
        }

        fun runescapeOrder(): Array<Skills> {
            return arrayOf(ATTACK, STRENGTH, DEFENCE, RANGED, PRAYER, MAGIC, RUNECRAFT, CONSTRUCTION, HITPOINTS,
                    AGILITY, HERBLORE, THIEVING, CRAFTING, FLETCHING, SLAYER, HUNTER, MINING, SMITHING, FISHING,
                    COOKING, FIREMAKING, WOODCUTTING, FARMING)
        }
    }
}

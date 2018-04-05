package rs.helpkit.plugins

import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.access.GameMenu
import rs.helpkit.api.game.access.Keyboard
import rs.helpkit.api.util.Renderable
import java.awt.Graphics2D

@Manifest(author = "Static", name = "Action Swapper", description = "Rearranges menu items", version = 1.0)
class ActionSwapper : Plugin(), Renderable {

    companion object {
        val BONE_ORDER = listOf(
                "Use Bones", "Use Burnt bones", "Use Wolf bones", "Use Bat bones", "Use Monkey bones",
                "Use Big bones", "Use Dagannoth bones", "Use Baby dragon bones", "Use Dragon bones",
                "Use Wyvern bones", "Use Lava dragon bones", "Use Superior dragon bones", "Use Jogre bones",
                "Use Zogre bones", "Use Fayrg bones", "Use Raurg bones", "Use Ourg bones", "Use Burnt jogre bones",
                "Use Pasty jogre bones", "Use Marinated j' bones", "Use Shaikahan bones"
        )

        val BANK_ORDER = listOf(
                "Exchange", "Bank Banker", "Bank Chest", "Bank Grand Exchange booth", "Bank Bank booth", "Collect"
        )

        val OBJECT_ORDER = listOf("Enter")

        val TAB_ORDER = listOf("Break", "Toggle Destination")

        val WEARABLE_ORDER = listOf("Wear", "Wield", "Remove")

        val FOOD_ORDER = listOf("Eat", "Drink")

        val JEWELLERY_ORDER = listOf("Wear", "Rub")

        val SPECIAL_ORDER = listOf(
                "Commune", "Check", "Teleport", "Fill", "Empty Ectophial", "Empty Coal bag", "Empty Herb sack",
                "Empty Small pouch", "Empty Medium pouch", "Empty Large pouch", "Empty Giant pouch", "Deposit",
                "Settings", "Features", "Disassemble", "Gem Mine"
        )

        val SKILL_ORDER = listOf("Clean", "Search")

        val NPC_ORDER = listOf("Pickpocket", "Attack")

        val SHIFT_ORDER = listOf(
                "Empty Herb sack", "Empty Coal bag", "Empty Small pouch", "Empty Medium pouch", "Empty Large pouch",
                "Empty Giant pouch", "Drop"
        )
    }

    override fun validate(): Boolean {
        return true
    }

    override fun onAwtCycle() {
        val options: MutableList<String> = ArrayList()
        if (Keyboard.holdingShift) {
            options.addAll(SHIFT_ORDER)
        }
        options.addAll(BONE_ORDER)
        options.addAll(BANK_ORDER)
        options.addAll(OBJECT_ORDER)
        options.addAll(TAB_ORDER)
        options.addAll(WEARABLE_ORDER)
        options.addAll(FOOD_ORDER)
        options.addAll(JEWELLERY_ORDER)
        options.addAll(SPECIAL_ORDER)
        options.addAll(SKILL_ORDER)
        options.addAll(NPC_ORDER)
        GameMenu.sort(*options.toTypedArray())
    }

    override fun render(g: Graphics2D) {

    }
}
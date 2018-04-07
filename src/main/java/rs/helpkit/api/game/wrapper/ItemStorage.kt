package rs.helpkit.api.game.wrapper

import rs.helpkit.api.raw.Fields
import rs.helpkit.api.raw.Wrapper
import rs.helpkit.api.game.wrapper.node.RSHashTable
import rs.helpkit.api.game.wrapper.node.RSNode


/**
 * @author Tyler Sedlar
 * @since 4/6/2018
 */
class ItemStorage(raw: Any) : Wrapper("ItemStorage", raw) {

    val ids
        get() = Fields.asIntArray("ItemStorage#ids", get())

    val stackSizes
        get() = Fields.asIntArray("ItemStorage#stackSizes", get())

    fun iterate(consumer: (item: RSItem) -> Unit) {
        val ids = this.ids
        val stacks = this.stackSizes
        if (ids != null && stacks != null) {
            for (i in 0 until Math.min(ids.size, stacks.size)) {
                val id = ids[i]
                val stack = stacks[i]
                if (id != -1) {
                    var def: Any? = null
                    cacheLookup(id)?.let {
                        if (it.validate()) {
                            def = it.get()
                        }
                    }
                    consumer(RSItem(this, i, id, stack, def))
                }
            }
        }
    }

    fun cacheLookup(id: Int): RSNode? {
        val cache = Fields["Client#itemDefinitionCache"]
        if (cache != null) {
            val table = RSHashTable(Fields["Cache#table", cache])
            if (table.validate()) {
                return table.iterator().findByUid(id)
            }
        }
        return null
    }

}

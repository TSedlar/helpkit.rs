package rs.helpkit.api.game.access

import rs.helpkit.api.game.wrapper.ItemStorage
import rs.helpkit.api.game.wrapper.node.RSHashTable
import rs.helpkit.api.game.wrapper.node.RSTableIterator
import rs.helpkit.api.raw.Fields

/**
 * @author Tyler Sedlar
 * @since 4/6/2018
 */
object ItemContainers {

    val VARROCK_GENERAL_STORE
        get() = lookup(4)
    val VARROCK_RUNE_STORE
        get() = lookup(5)
    val VARROCK_STAFF_STORE
        get() = lookup(51)
    val PRICE_CHECKER
        get() = lookup(90)
    val INVENTORY
        get() = lookup(93)
    val EQUIPMENT
        get() = lookup(94)
    val BANK
        get() = lookup(95)
    val EXCHANGE_COLLECTION
        get() = lookup(518)

    private val TABLES: RSHashTable
        get() = RSHashTable(Fields["Client#inventoryTable"])

    fun lookup(uid: Int): ItemStorage? {
        val storage = TABLES
        if (storage.validate()) {
            val itr = RSTableIterator(storage)
            val lookup = itr.findByUid(uid)
            if (lookup != null && lookup.validate()) {
                return ItemStorage(lookup.get()!!)
            }
        }
        return null
    }
}
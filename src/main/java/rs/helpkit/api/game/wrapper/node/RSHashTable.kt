package rs.helpkit.api.game.wrapper.node

import rs.helpkit.api.raw.Wrapper

class RSHashTable(referent: Any?) : Wrapper("HashTable", referent) {

    fun head(): RSNode = RSNode(this["head"])

    fun tail(): RSNode = RSNode(this["tail"])

    fun buckets(): List<RSNode>? = asArray("buckets")?.map { node -> RSNode(node) }
}
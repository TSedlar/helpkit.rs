package rs.helpkit.api.game.wrapper.node

class RSTableIterator(private val table: RSHashTable) {

    private var index: Int = 0
    private var current: RSNode? = null

    fun first(): RSNode? {
        index = 0
        return next()
    }

    operator fun next(): RSNode? {
        if (table.validate()) {
            val buckets = table.buckets()
            if (buckets == null || buckets.isEmpty()) {
                return null
            }
            if (index > 0 && current != null && current!!.validate()) {
                val prev = buckets[index - 1]
                if (prev.validate() && current!!.get()!!.hashCode() != prev.get()!!.hashCode()) {
                    val node = current
                    current = node!!.previous()
                    return node
                }
            }
            while (index < buckets.size) {
                val next = buckets[index++]
                val node = next.previous()
                if (!node.validate() || index - 1 >= buckets.size) {
                    return null
                }
                val check = buckets[index - 1]
                if (check.get()!!.hashCode() != node.get()!!.hashCode()) {
                    current = node.previous()
                    return node
                }
            }
        }
        return null
    }

    fun findByUid(id: Int): RSNode? {
        return findByFilter({ node -> node.uid() == id.toLong() })
    }

    fun lookup(key: Int): RSNode? {
        if (table.validate()) {
            val buckets = table.buckets()
            return buckets!![key and buckets.size - 1]
        }
        return null
    }

    fun findByFilter(filter: (node: RSNode) -> Boolean): RSNode? {
        if (table.validate()) {
            var node = first()
            while (node != null && node.validate()) {
                if (filter(node)) {
                    return node
                }
                node = next()
            }
        }
        return null
    }
}

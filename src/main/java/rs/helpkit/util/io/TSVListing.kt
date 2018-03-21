package rs.helpkit.util.io

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * @author Tyler Sedlar
 * @since 8/6/2017
 */
class TSVListing {

    val lines: MutableList<Array<String>> = ArrayList()

    companion object {

        fun load(file: String): TSVListing {
            val mapping = TSVListing()
            try {
                Files.lines(Paths.get(file)).forEach { line -> mapping.lines.add(line.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) }
            } catch (e: IOException) {
                throw IllegalStateException("Unable to parse tsv: " + file)
            }

            return mapping
        }
    }
}
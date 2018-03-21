package rs.helpkit.dev.services

import rs.helpkit.api.util.Time
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
import java.nio.file.StandardWatchEventKinds.OVERFLOW

/**
 * @since 03/20/2018
 */
class HookReloaderService(
        private val hookDirectory: String,
        private val callback: () -> Unit
): Thread() {
    override fun run() {
        val watcher = FileSystems.getDefault().newWatchService()

        Paths.get(hookDirectory).register(watcher, ENTRY_MODIFY)

        while (true) {
            val key = try {
                val k = watcher.poll()
                Time.sleep(50)
                k ?: continue
            } catch (e: InterruptedException) {
                // this is okay I think
                continue
            }

            key.pollEvents().forEach { it ->
                when (it.kind()) {
                    OVERFLOW -> return@forEach
                    ENTRY_MODIFY -> {
                        callback()
                    }
                }
                val path = it.context() as Path
                println("[${it.kind().name()}] $path")
            }

            val valid = key.reset()
            if (!valid) {
                error("$hookDirectory no longer accessible")
            }
        }
    }
}

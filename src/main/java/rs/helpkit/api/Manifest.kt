package rs.helpkit.api


@kotlin.annotation.Retention(kotlin.annotation.AnnotationRetention.RUNTIME)
annotation class Manifest(
        val author: String,
        val name: String,
        val description: String,
        val version: Double,
        val loop: Boolean = true
)

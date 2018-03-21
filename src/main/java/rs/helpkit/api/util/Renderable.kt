package rs.helpkit.api.util

import org.jetbrains.annotations.NotNull
import java.awt.Graphics2D

interface Renderable {

    fun render(@NotNull g: Graphics2D)
}

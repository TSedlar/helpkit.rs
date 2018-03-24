package rs.helpkit.plugins

import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.Camera
import rs.helpkit.api.util.Schedule

@Manifest(author = "Static", name = "Camera Zoom", description = "Synchronizes the camera zoom", version = 1.0)
class CameraZoom : Plugin() {

    override fun validate(): Boolean {
        return true
    }

    @Schedule(250)
    fun setCameraZoom() = Camera.setZoom()
}

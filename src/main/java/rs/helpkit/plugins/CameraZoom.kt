package rs.helpkit.plugins

import rs.helpkit.api.Manifest
import rs.helpkit.api.Plugin
import rs.helpkit.api.game.access.Camera

@Manifest(author = "Static", name = "Camera Zoom", description = "Bypasses the camera zoom limit", version = 1.0)
class CameraZoom: Plugin() {

    override fun validate(): Boolean {
        return true
    }

    override fun onAwtCycle() {
        Camera.setZoom()
    }
}
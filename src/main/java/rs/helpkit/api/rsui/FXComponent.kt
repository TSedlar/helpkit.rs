package rs.helpkit.api.rsui

import rs.helpkit.api.util.Renderable

abstract class FXComponent : Renderable {

    var x: Int = 0
    var y: Int = 0
    var w: Int = 0
    var h: Int = 0
    var visible: Boolean = true

    var width: Int
        get() = this.w
        set(w) {
            this.w = w
        }
    var height: Int
        get() = this.h
        set(h) {
            this.h = h
        }
}
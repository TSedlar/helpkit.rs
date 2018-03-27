package rs.helpkit.api.rsui

class RSOctoCheck(x: Int, y: Int) : RSImage(RSUI.CHECK_OCTO, null, x, y) {

    var selected = false
        set(value) {
            if (field != value) {
                field = value
                registerChange()
            }
        }

    private var callback: ((Boolean) -> Unit)? = null

    init {
        super.mouseListeners.clear()
        super.mouseMotionListeners.clear()
        onClick { _, _ ->
            selected = !selected
        }
    }

    private fun registerChange() {
        callback?.invoke(selected)
        super.currentImage = if (selected) {
            RSUI.CHECK_OCTO_ON
        } else {
            RSUI.CHECK_OCTO
        }
    }

    fun onValueChange(callback: (Boolean) -> Unit): RSOctoCheck {
        this.callback = callback
        return this
    }
}
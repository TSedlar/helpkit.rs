package rs.helpkit.api.rsui

/**
 * @author Tyler Sedlar
 * @since 3/26/2018
 */
object RSUI {

    val CLOSE_BTN = "/images/ui/close-button.png"
    val CLOSE_BTN_HOVER = "/images/ui/close-button-h.png"

    fun closeButton(x: Int, y: Int): RSImage = RSImage(CLOSE_BTN, CLOSE_BTN_HOVER, x, y)
}
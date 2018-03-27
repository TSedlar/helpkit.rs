package rs.helpkit.api.rsui

import rs.helpkit.util.io.Resources

/**
 * @author Tyler Sedlar
 * @since 3/26/2018
 */
object RSUI {

    val CLOSE_BTN = Resources.img("/images/ui/close-button.png")
    val CLOSE_BTN_HOVER = Resources.img("/images/ui/close-button-h.png")

    val CHECK_OCTO = Resources.img("/images/ui/check-octo-off.png")
    val CHECK_OCTO_ON = Resources.img("/images/ui/check-octo-on.png")

    val REFRESH_ONE = Resources.img("/images/ui/refresh-one.png")
    val REFRESH_ALL = Resources.img("/images/ui/refresh.png")

    val TRASH = Resources.img("/images/ui/trash.png")

    val DELETE_ONE = Resources.img("/images/ui/delete-one.png")

    fun closeButton(x: Int, y: Int): RSImage = RSImage(CLOSE_BTN, CLOSE_BTN_HOVER, x, y)

//    fun checkOctoButton(x: Int, y: Int): RSImage = RSImage()
}
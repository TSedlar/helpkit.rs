package rs.helpkit.api.game.access

import rs.helpkit.api.raw.Methods

/**
 * @author Tyler Sedlar
 * @since 3/26/2018
 */
object Chatbox {

    val MESSAGE_SERVER = 0
    val MESSAGE_CHAT = 2
    val MESSAGE_PRIVATE_INCOMING = 3
    val MESSAGE_TRADE_INCOMING = 4
    val MESSAGE_PRIVATE_INFO = 5
    val MESSAGE_PRIVATE_SENT = 6
    val MESSAGE_CLANCHAT = 9
    val MESSAGE_CLIENT = 11
    val MESSAGE_TRADE_SENT = 12
    val MESSAGE_EXAMINE_ITEM = 27
    val MESSAGE_EXAMINE_NPC = 28
    val MESSAGE_EXAMINE_OBJECT = 29
    val MESSAGE_AUTOCHAT = 90
    val MESSAGE_SERVER_FILTERED = 105
    val MESSAGE_ACTION = 109

    val MAX_CHANNEL_COUNT = 8

    val COLOR_WARN = "9b2011"

    fun sendMessage(msg: String) {
        Methods.invoke("Client#addMessage", null, MESSAGE_SERVER, "", msg, null)
    }

    fun sendMessage(msg: String, color: String) {
        sendMessage("<col=$color>$msg")
    }
}
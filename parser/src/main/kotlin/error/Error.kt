package error

import components.Position
import components.Token

class Error(message: String, private val token: Token) : Exception(message) {

    fun getTokenPosition(): Position {
        return token.getPosition()
    }
}
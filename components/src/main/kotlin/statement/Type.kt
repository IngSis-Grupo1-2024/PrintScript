package components.statement

import components.Position
import components.TokenType

class Type(private val value: TokenType, private val position: Position) {
    fun getValue(): TokenType = value

    fun getPosition(): Position = position

    override fun toString(): String {
        return "Type(value='$value')"
    }
}

package components.statement

import components.Position
import components.TokenType

interface Value {

    fun getValue(): String
    fun getPosition(): Position
    fun getType() : TokenType
}

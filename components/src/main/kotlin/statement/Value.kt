package components.statement

import components.Position
import components.TokenType

interface Value<T> {

    fun getValue(): String
    fun getPosition(): Position
    fun getType() : TokenType
}

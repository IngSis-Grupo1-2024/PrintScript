package components.statement

import components.Position
import components.TokenType

class SingleValue(private val value:String, private val position: Position, private val type :TokenType):Value {

    override fun getValue(): String = value

    override fun getPosition(): Position = position

    override fun getType() : TokenType = type

}
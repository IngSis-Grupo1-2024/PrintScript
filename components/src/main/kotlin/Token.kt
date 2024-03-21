package components

class Token (val position: Position, val value: String, val type: TokenType) {

    override fun toString(): String =  "type: $type, value: $value"

    fun getType(): TokenType = type
    fun getValue(): String = value
}
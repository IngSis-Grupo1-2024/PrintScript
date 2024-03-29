package components

class Token(private val position: Position, private val value: String, private val type: TokenType) {
    override fun toString(): String = "type: $type, value: $value"

    fun getType(): TokenType = type

    fun getValue(): String = value

    fun getPosition(): Position = position
}

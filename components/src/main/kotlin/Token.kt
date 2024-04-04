package components

class Token(private val position: Position, private val value: String, private val type: TokenType) {
    override fun toString(): String = "type: $type, value: $value"

    fun getType(): TokenType = type

    fun getValue(): String = value

    fun getPosition(): Position = position

    fun copy(position: Position = this.position, value: String = this.value, type: TokenType = this.type): Token {
        return Token(position, value, type)
    }

}
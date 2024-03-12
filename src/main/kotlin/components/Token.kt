package components

class Token (private val position: Position, private val value: String, val type: TokenType) {

    override fun toString(): String {
        return "$type"
    }

}
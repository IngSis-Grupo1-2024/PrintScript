package components

class Token (val position: Position, val value: String, val type: TokenType) {

    override fun toString(): String {
        return "$type"
    }

}
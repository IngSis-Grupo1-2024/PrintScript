package components.statement

import components.Position

class Type(private val value: String, private val position: Position) {
    fun getValue(): String = value

    fun getPosition(): Position = position
    override fun toString(): String {
        return "Type(value='$value')"
    }

}

package components.statement

import components.Position

class Variable(private val name: String, private val position: Position) {
    override fun toString(): String {
        return "\tname: $name"
    }

    fun getName(): String = name

    fun getPosition(): Position = position
}

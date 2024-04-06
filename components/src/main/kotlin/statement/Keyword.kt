package components.statement

import components.Position

class Keyword(private val modifier: Modifier,
              private val value: String,
              private val position: Position) {
    fun getModifier(): Modifier = modifier
    fun getValue(): String = value
    fun getPosition(): Position = position
    override fun toString(): String {
        return "modifier: " + modifier + "value: " + value
    }
}

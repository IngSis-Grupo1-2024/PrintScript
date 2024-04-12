package result

import components.Position

class InvalidResult(private val position: Position, private val message: String) : Result {
    fun getPosition(): Position = position

    fun getMessage(): String = message
}

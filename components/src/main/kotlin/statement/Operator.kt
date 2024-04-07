package components.statement

import components.Position
import components.TokenType

class Operator(
    private val value: String,
    private val position: Position,
    private val type: TokenType,
    private val leftOperator: Value,
    private val rightOperator: Value
) : Value {


    override fun getValue(): String {
        return value
    }

    override fun getPosition(): Position {
        return position
    }

    override fun getType(): TokenType {
        return type
    }

    fun getLeftOperator(): Value = leftOperator

    fun getRightOperator(): Value = rightOperator

}
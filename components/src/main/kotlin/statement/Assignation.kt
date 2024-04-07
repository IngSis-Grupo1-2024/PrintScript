package components.statement

import components.Position

class Assignation(private val position: Position, private val variable: Variable, private val value:Value) : Statement {

    val statementType = StatementType.ASSIGNATION

    fun getVariable(): Variable = variable

    fun getValue(): Value = value

    fun getPosition(): Position = position

    override fun toString(): String =
        "name: ${variable.getName()} \t  value: $value"
}
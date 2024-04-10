package components.statement

import components.Position

class Assignation(private val variable: Variable, private val value: Value, private val position: Position) :
    Statement {


    fun getVariable(): Variable = variable

    fun getValue(): Value = value

    fun getPosition(): Position = position

    override fun getStatementType(): StatementType {
        return StatementType.ASSIGNATION
    }

    override fun toString(): String =
        "name: ${variable.getName()} \t  value: $value"
}
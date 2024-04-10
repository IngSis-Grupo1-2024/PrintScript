package components.statement

import components.Position

class Assignation(private val position: Position, private val variable: Variable, private val value: Value) :
    Statement {
    fun getVariable(): Variable = variable

    fun getValue(): Value = value

    fun getPosition(): Position = position

    override fun getStatementType(): StatementType {
        return StatementType.ASSIGNATION
    }

    override fun toString(): String = "name: ${variable.getName()} \t  value: $value"
}

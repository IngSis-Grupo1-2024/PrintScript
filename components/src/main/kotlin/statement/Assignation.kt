package components.statement

class Assignation(private val variable: Variable, private val value:Value) : Statement {

    val statementType = StatementType.ASSIGNATION

    fun getVariable(): Variable = variable

    fun getValue(): Value = value

    override fun toString(): String =
        "name: ${variable.getName()} \t  value: $value"
}
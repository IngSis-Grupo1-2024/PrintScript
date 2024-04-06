package components.statement

class Assignation<T>(private val variable: Variable, private val value:Value<T>) : Statement {

    val statementType = StatementType.ASSIGNATION

    fun getVariable(): Variable = variable

    fun getValue(): Value<T> = value

    override fun toString(): String = "name: ${variable.getName()} \t  value: ${value.getValue()}"

    // 3 * 8

}
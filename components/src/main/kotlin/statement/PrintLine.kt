package components.statement

class PrintLine(private val value: Value) : Statement {

    val statementType = StatementType.PRINT_LINE

    fun getValue(): Value = value

    override fun toString(): String {
        return "PrintLine()"
    }
}
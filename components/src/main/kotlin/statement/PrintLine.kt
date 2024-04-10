package components.statement

import components.Position

class PrintLine(private val position: Position, private val value: Value) : Statement {
    private val statementType = StatementType.PRINT_LINE

    fun getValue(): Value = value

    fun getPosition(): Position = position

    override fun getStatementType(): StatementType = statementType

    override fun toString(): String {
        return "PrintLine(position=$position, value=$value, statementType=$statementType)"
    }
}

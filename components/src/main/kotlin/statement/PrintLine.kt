package components.statement

import components.Token

class PrintLine(private val token: Token, val value: Value) : Statement {
    private val statementType = StatementType.PRINT_LINE

    fun getValue(): Value = value

    fun getToken(): Token = token

    override fun getStatementType(): StatementType = statementType

    override fun toString(): String {
        return "PrintLine()"
    }
}

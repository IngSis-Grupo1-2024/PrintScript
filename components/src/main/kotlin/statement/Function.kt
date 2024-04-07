package components.statement

import components.Token

class Function(private val token: Token, private val value: Value) : Statement {
    val statementType = StatementType.FUNCTION

    fun getValue(): Value = value

    fun getToken(): Token = token

    override fun toString(): String = "Function(value=$value)"
}
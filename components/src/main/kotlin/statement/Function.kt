package components.statement

import components.Token

class Function(private val token: Token, private val value:Value) : Statement {


    fun getValue():Value = value

    fun getToken():Token = token

    override fun getStatementType(): StatementType {
        return StatementType.FUNCTION
    }

    override fun toString(): String =
        "Function(value=$value)"

}
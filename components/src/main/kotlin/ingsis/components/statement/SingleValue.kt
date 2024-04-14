package ingsis.components.statement

import ingsis.components.Token

class SingleValue(private val token: Token) : Value {
    override fun isEmpty(): Boolean = false

    override fun getToken(): Token = token

    override fun getChildrenAmount(): Int = 0

    override fun isLeaf(): Boolean = true

    override fun addChildren(ast: Value): Value = Operator(token, ast)

    override fun toString(): String {
        return "SingleValue(token=$token)"
    }
}

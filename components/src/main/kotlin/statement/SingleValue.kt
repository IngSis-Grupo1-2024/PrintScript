package components.statement

import components.Token

class SingleValue(private val token: Token) : Value {
    override fun isEmpty(): Boolean {
        return true
    }

    override fun getToken(): Token {
        return token
    }

    override fun getChildrenAmount(): Int {
        TODO("Not yet implemented")
    }

    override fun isLeaf(): Boolean {
        return true
    }

    override fun addChildren(ast: Value): Value {
        TODO("Not yet implemented")
    }
}

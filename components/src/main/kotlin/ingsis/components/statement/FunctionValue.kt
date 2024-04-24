package ingsis.components.statement

import ingsis.components.Token

class FunctionValue(private val value: Function) : Value {
    override fun isEmpty(): Boolean {
        return value.getValue() == EmptyValue()
    }

    override fun getToken(): Token {
        return value.getToken()
    }

    override fun getChildrenAmount(): Int {
        return 1
    }

    override fun isLeaf(): Boolean {
        return true
    }

    override fun addChildren(ast: Value): Value {
        throw Exception("It's not possible to add more arguments inside this function")
    }
}

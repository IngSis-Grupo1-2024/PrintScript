package components.statement

import components.Token

class EmptyValue : Value {
    override fun isEmpty(): Boolean = true

    override fun getToken(): Token {
        throw NullPointerException("Value is null")
    }

    override fun getChildrenAmount(): Int = 0

    override fun isLeaf(): Boolean {
        throw NullPointerException("Value is null")
    }

    override fun addChildren(ast: Value): Value = ast

    override fun toString(): String {
        return "EmptyValue()"
    }
}

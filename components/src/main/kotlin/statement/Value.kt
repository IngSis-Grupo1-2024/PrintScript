package components.statement

import components.Token

interface Value {
    fun isEmpty(): Boolean

    fun getToken(): Token

    fun getChildrenAmount(): Int

    fun isLeaf(): Boolean

    fun addChildren(ast: Value): Value
}

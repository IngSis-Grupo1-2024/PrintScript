package components.statement

import components.Token

class Operator(
    private val token: Token,
    private val leftOperator: Value,
    private val rightOperator: Value
) : Value {

    constructor(token: Token) : this(token, EmptyValue(), EmptyValue())
    constructor(token: Token, leftOperator: Value) : this(token, EmptyValue(), EmptyValue())

    override fun isEmpty(): Boolean = false

    override fun getToken(): Token = token

    override fun getChildrenAmount(): Int {
        return if(isLeaf()) 0
        else if(rightOperator.isEmpty()) 1 else 2
    }

    override fun isLeaf(): Boolean = leftOperator.isEmpty() && rightOperator.isEmpty()

    override fun addChildren(ast: Value): Value {
        if(isEmpty()) return ast
        else if(isLeaf()) return Operator(token, ast)
        return Operator(token, leftOperator, ast)
    }

    fun getLeftOperator(): Value {
        if(!leftOperator.isEmpty()) return leftOperator
        throw NullPointerException("Left Operator is null")
    }

    fun getRightOperator(): Value {
        if(!rightOperator.isEmpty()) return rightOperator
        throw NullPointerException("Right Operator is null")
    }

    fun getLastOperator(): Value {
        if(isLeaf()) throw NullPointerException("Left Operator is null")
        else if(rightOperator.isEmpty()) return leftOperator
        return rightOperator
    }

    fun replace(value: Value, other: Value): Operator{
        return if(rightOperator.isEmpty() && leftOperator == value) Operator(token, other)
        else if(rightOperator == value) Operator(token, leftOperator, other)
        else throw NullPointerException("there is no operator as value")
    }

    override fun toString(): String {
        return "\n\tOperator(\n\ttoken=$token, \n\tleftOperator=$leftOperator, \n\trightOperator=$rightOperator)"
    }


}
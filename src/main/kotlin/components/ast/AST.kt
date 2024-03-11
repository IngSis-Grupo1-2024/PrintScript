package components.ast

import components.Token

class AST(val token: Token, val left: ASTInterface?, val right: ASTInterface?) : ASTInterface {

    fun constructor(token: Token): AST {
        return AST(token, null, null)
    }

    fun constructor(token: Token, left: AST): AST {
        return AST(token, left, null)
    }

    fun constructor(token: Token, left: AST, right: AST): AST {
        return AST(token, left, right)
    }
    override fun isLeaf(): Boolean {
        return left == null && right == null
    }

    override fun toString(): String {
        return "{\n\ttoken: $token,\n\tleft: $left,\n\tright: $right\n}"
    }
}
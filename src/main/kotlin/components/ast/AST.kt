package components.ast

import components.Token

class AST(override val token: Token, override val left: ASTInterface?, override val right: ASTInterface?, private val additionalChildren: List<ASTInterface> = ArrayList<ASTInterface>()) : ASTInterface {
    constructor(token: Token, left: ASTInterface) : this(token, left, null)

    constructor(token: Token): this(token, null, null)



    override fun isLeaf(): Boolean {
        return left == null && right == null
    }

    override fun addChildren(ast: ASTInterface): ASTInterface {
        return if(isLeaf()) AST(token, ast)
        else if(left == null) AST(token, ast, right!!)
        else if(right == null) AST(token, left, ast)
        else if(additionalChildren.isEmpty()) AST(token, left, right, listOf(ast))
        else AST(token, left, right, additionalChildren + listOf(ast))
    }

    override fun hasAnyEmptyChild(): Boolean {
        return (left == null).xor(right == null)
    }

    override fun toString(): String {
        return "{\n\ttoken: $token,\n\tleft: $left,\n\tright: $right\n"
    }



}
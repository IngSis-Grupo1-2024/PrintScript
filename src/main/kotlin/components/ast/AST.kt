package components.ast

import components.Token

class AST(override val token: Token?, override val children: List<ASTInterface>) : ASTInterface {

    constructor(): this(null, emptyList())
    constructor(token: Token, children: ASTInterface) : this(token, listOf(children))
    constructor(token: Token): this(token, emptyList())

    override fun isLeaf(): Boolean {
        return children.isEmpty()
    }
    override fun isEmpty(): Boolean {
        return token == null && isLeaf()
    }
    override fun addChildren(ast: ASTInterface): ASTInterface {
        return if(isEmpty()) ast
        else if (isLeaf()) AST(token!!, ast)
        else AST(token!!, children + listOf(ast))
    }

    override fun childrenAmount(): Int {
        return children.size
    }

    override fun toString(): String {
        return "{\n\ttoken: $token, \n\ttoken_value:${token?.value},\n\tchildren: $children\n}}"
    }

    override fun removeChildren(ast: ASTInterface): ASTInterface {
        val newChildren = mutableListOf<ASTInterface>()
        for (child in children) {
            if (child != ast) {
                newChildren.add(child)
            }
        }
        return AST(token, newChildren)
    }
}
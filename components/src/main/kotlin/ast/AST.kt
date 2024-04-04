package components.ast

import components.Token

class AST(private val token: Token?, private val children: List<ASTInterface>) : ASTInterface {
    constructor() : this(null, emptyList())
    constructor(token: Token, children: ASTInterface) : this(token, listOf(children))
    constructor(token: Token) : this(token, emptyList())

    override fun getToken(): Token {
        if (token == null) {
            throw NullPointerException("Token is null")
        } else {
            return token
        }
    }

    override fun getChildren(): List<ASTInterface> = this.children

    override fun isLeaf(): Boolean = this.children.isEmpty()

    override fun isEmpty(): Boolean = this.token == null && isLeaf()

    override fun addChildren(ast: ASTInterface): ASTInterface {
        return if (isEmpty()) {
            ast
        } else if (isLeaf()) {
            AST(getToken(), ast)
        } else {
            AST(getToken(), this.children + listOf(ast))
        }
    }

    override fun getChildrenAmount(): Int = this.children.size

    override fun toString(): String = "{\n\ttoken: $token,\n\tchildren: $children\n}}"

    override fun replace(
        ast: ASTInterface,
        other: ASTInterface,
    ): ASTInterface {
        val newChildren = mutableListOf<ASTInterface>()
        for (child in children) {
            if (child == ast) {
                newChildren.add(other)
            } else {
                newChildren.add(child)
            }
        }
        return AST(token, newChildren)
    }

    override fun copy(
        newToken: Token?,
        children: List<ASTInterface>,
    ): AST {
        return AST(newToken, children)
    }
}

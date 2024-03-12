package components.ast

import components.Token

interface ASTInterface {
    val token: Token
    val left: ASTInterface?
    val right: ASTInterface?

    fun isLeaf() : Boolean

    fun addChildren(ast: ASTInterface): ASTInterface
}
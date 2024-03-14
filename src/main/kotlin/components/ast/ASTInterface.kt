package components.ast

import components.Token

interface ASTInterface {
    val token: Token?
    val children: List<ASTInterface>

    fun isLeaf() : Boolean
    fun isEmpty() : Boolean
    fun addChildren(ast: ASTInterface): ASTInterface
    fun childrenAmount(): Int
    fun removeChildren(ast: ASTInterface): ASTInterface
}
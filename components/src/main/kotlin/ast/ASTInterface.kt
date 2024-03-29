package components.ast

import components.Token

interface ASTInterface {
    fun getToken(): Token

    fun getChildren(): List<ASTInterface>

    fun isLeaf(): Boolean

    fun isEmpty(): Boolean

    fun addChildren(ast: ASTInterface): ASTInterface

    fun childrenAmount(): Int

    fun replace(
        ast: ASTInterface,
        other: ASTInterface,
    ): ASTInterface
}

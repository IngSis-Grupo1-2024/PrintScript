package components

interface ASTInterface {

    val token: Token
    val left: ASTInterface?
    val right: ASTInterface?

    fun isLeaf() : Boolean

}
package components

interface AST {

    val token: Token
    val left: AST?
    val right: AST?

    fun getToken(): Token

    fun getLeft(): AST?

    fun getRight(): AST?

    fun put(token: Token, left: AST?, right: AST?)

}
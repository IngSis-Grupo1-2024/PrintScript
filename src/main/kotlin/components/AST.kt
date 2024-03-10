package components

interface AST {

    val token: Token
    val left: AST?
    val right: AST?

    fun put(token: Token, left: AST?, right: AST?)

}
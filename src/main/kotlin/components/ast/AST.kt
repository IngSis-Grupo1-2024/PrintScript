package components.ast

import components.Token

class AST(override val token: Token, override val left: ASTInterface?, override val right: ASTInterface?) : ASTInterface {
    constructor(token: Token, left: ASTInterface) : this(token, left, null)

    constructor(token: Token): this(token, null, null)


    override fun isLeaf(): Boolean {
        return left == null && right == null
    }

    override fun toString(): String {
        return "{\n\ttoken: $token,\n\tleft: $left,\n\tright: $right\n}"
    }
}
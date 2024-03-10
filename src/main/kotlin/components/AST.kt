package components

class AST(override val token: Token, override val left: ASTInterface?, override val right: ASTInterface?) : ASTInterface {

    override fun isLeaf(): Boolean {
        return left == null && right == null
    }

    override fun toString(): String {
        return "{\n\ttoken: $token,\n\tleft: $left,\n\tright: $right\n}"
    }
}
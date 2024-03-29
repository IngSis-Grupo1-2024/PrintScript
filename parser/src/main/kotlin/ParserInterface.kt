import components.Token
import components.ast.ASTInterface

interface ParserInterface {
    fun parse(tokens: List<Token>): ASTInterface
}

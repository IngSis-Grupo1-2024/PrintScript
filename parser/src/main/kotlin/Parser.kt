import components.*
import components.ast.AST
import components.ast.ASTInterface
import kotlin.math.abs

class Parser : ParserInterface {
    private val typeComparator = ComparatorTokenType()
    private val valueComparator = ComparatorTokenValue()
    private val twoChildToken = listOf(TokenType.ASSIGNATION, TokenType.OPERATOR)
    override fun parse(tokens: List<Token>): ASTInterface {
        var ast: ASTInterface = getEmptyAST()
        if(tokens.size > 1)
            for (token in tokens.subList(1, tokens.size)) {
                ast = add(token, ast)
            }
        return ast
    }

    private fun getEmptyAST(): ASTInterface {
        return AST()
    }

    private fun getLeaf(token: Token): ASTInterface = AST(token)


    // lo que podes hacer es chequear con los hijos
    // sabes que si hay un identifier, un type o un const (o value) esos si o si
    // tienen que ser hoja, por lo que si se encuentran con otro, lo mandas como hermano
    private fun add(token: Token, ast: ASTInterface): ASTInterface {
        if(ast.isEmpty()) return ast.addChildren(getLeaf(token))
        val compareTokens = compareValueAndType(token, ast)
        return if (rootIsBigger(compareTokens)) compWChildren(token, ast)
        else if (compareTokens == 1) AST(token, ast)
        else if (abs(compareTokens) == 2) ast
        else ast.addChildren(getLeaf(token))
    }
    private fun compareValueAndType(token: Token, ast: ASTInterface): Int {
        val compValue = valueComparator.compare(token, ast.token)
        val compToken = typeComparator.compare(token.type, ast.token!!.type)
        return if(compToken != 0) compToken
        else compValue
    }
    private fun compWChildren(token: Token, ast: ASTInterface) : ASTInterface {
        val rootToken = ast.token
        if(rootToken!!.type in twoChildToken && has1or0Child(ast)) return findEmptyLeaf(token, ast)
        for(child in ast.children){
            val comp = compareValueAndType(token, child)
            if (comp != 0) return ast.removeChildren(child).addChildren(add(token, child))
        }
        return ast.addChildren(getLeaf(token))
    }
    private fun has1or0Child(ast: ASTInterface): Boolean = ast.childrenAmount() == 1 || ast.isLeaf()
    private fun findEmptyLeaf(token: Token, ast: ASTInterface) : ASTInterface =
        ast.addChildren(getLeaf(token))
    private fun rootIsBigger(compareTokens: Int) = compareTokens == -1
    private fun isDeclaration(tokens: List<Token>): Boolean {
        // this would be the declaration structure:
        // let x: string;
        return tokens[1].type == TokenType.IDENTIFIER // x
                && tokens[2].type == TokenType.DECLARATION // :
                && tokens[3].type == TokenType.TYPE // string
                && tokens[4].type == TokenType.SEMICOLON
    }

    private fun isAssignation(tokens: List<Token>): Boolean {
        // there are 2 options for assignation:
        // declaration + assignation OR just declaration
        return (tokens[1].type == TokenType.IDENTIFIER // x
                && tokens[2].type == TokenType.DECLARATION // :
                && tokens[3].type == TokenType.TYPE // string
                && tokens[4].type == TokenType.ASSIGNATION // =
                && tokens[5].type == TokenType.VALUE // "hello"
                && tokens[tokens.size - 1].type == TokenType.SEMICOLON)
                ||
                (tokens[0].type == TokenType.IDENTIFIER
                 && tokens[1].type == TokenType.ASSIGNATION
                 && tokens[2].type == TokenType.VALUE
                 && tokens[tokens.size - 1].type == TokenType.SEMICOLON)
    }

    private fun isCallingMethod(tokens: List<Token>): Boolean {
        // this would be the calling function structure that comes from lexer:
        // print("hello")
        // sum(a + b)
        return tokens[0].type == TokenType.KEYWORD // println or sum
                && tokens[1].type == TokenType.PARENTHESIS // (
                && tokens[tokens.size - 2].type == TokenType.PARENTHESIS // )
                && tokens[tokens.size - 1].type == TokenType.SEMICOLON
    }

}
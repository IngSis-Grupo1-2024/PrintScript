import components.*
import components.ast.AST
import components.ast.ASTInterface
import kotlin.math.abs

class Parser : ParserInterface {
    private val typeComparator = ComparatorTokenType()
    private val valueComparator = ComparatorTokenValue()
    private val doesntMatterTypes = listOf(TokenType.PARENTHESIS, TokenType.KEYWORD)

    override fun parse(tokens: List<Token>): ASTInterface {
        var ast: ASTInterface = getEmptyAST()
        if (tokens.size > 1) {
            for (token in tokens.subList(1, tokens.size))
                ast = add(token, ast)
        }
        return ast
    }

    fun transformDeclaration(tokens: List<Token>): ASTInterface {
        val decl = tokens[2]
        val identifier = tokens[1]
        val value = tokens[3]
        return AST(decl, listOf(getLeaf(identifier), getLeaf(value)))
    }

    fun transformAssignation(tokens: List<Token>): ASTInterface {
        val assignIndex: Int = findAssignIndex(tokens)
        val ident: ASTInterface = transformIdent(tokens.subList(0, assignIndex))
        val assign = tokens[assignIndex]
        val value = transformValue(tokens.subList(assignIndex + 1, tokens.size - 1))
        return AST(assign, listOf(ident, value))
    }

    fun transformFunction(tokens: List<Token>): ASTInterface {
        val function = tokens[0]
        val value = transformValue(tokens.subList(1, tokens.size - 1))
        return AST(function, listOf(value))
    }

    private fun transformIdent(tokens: List<Token>): ASTInterface {
        if (tokens.size == 1) return getLeaf(tokens[0])
        return transformDeclaration(tokens)
    }

    private fun findAssignIndex(tokens: List<Token>): Int {
        for (i in 0..tokens.size)
            if (tokens[i].getType() == TokenType.ASSIGNATION) return i
        return 0
    }

    private fun transformValue(tokens: List<Token>): ASTInterface {
        if (tokens.size == 1) return getLeaf(tokens[0])
        return getTree(tokens)
    }

    private fun getTree(tokens: List<Token>): ASTInterface {
        var ast = getEmptyAST()
        for (token in tokens) {
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
    private fun add(
        token: Token,
        ast: ASTInterface,
    ): ASTInterface {
        if (token.getType() in doesntMatterTypes) return ast
        if (ast.isEmpty()) return ast.addChildren(getLeaf(token))
        val compareTokens = compareValueAndType(token, ast.getToken())
        return if (rootIsBigger(compareTokens)) {
            compWChildren(token, ast)
        } else if (compareTokens == 1) {
            AST(token, ast)
        } else if (abs(compareTokens) == 2) {
            ast
        } else {
            ast.addChildren(getLeaf(token))
        }
    }

    private fun compareValueAndType(
        token: Token,
        root: Token,
    ): Int {
        val compValue = valueComparator.compare(token, root)
        val compToken = typeComparator.compare(token.getType(), root.getType())
        return if (compToken != 0) {
            compToken
        } else {
            compValue
        }
    }

    private fun compWChildren(
        token: Token,
        ast: ASTInterface,
    ): ASTInterface {
        for (child in ast.getChildren()) {
            val comp = compareValueAndType(token, child.getToken())
            when (comp) {
                -1 -> return ast.replace(child, add(token, child))
                1 -> return removeLastChild(ast, token)
            }
        }
        return ast.addChildren(getLeaf(token))
    }

    private fun removeLastChild(
        ast: ASTInterface,
        token: Token,
    ): ASTInterface {
        val lastChild = ast.getChildren().last()
        return ast.replace(lastChild, add(token, lastChild))
    }

    private fun rootIsBigger(compareTokens: Int) = compareTokens == -1

    fun isDeclaration(tokens: List<Token>): Boolean {
        // this would be the declaration structure:
        // let x: string;
        return tokens[1].getType() == TokenType.IDENTIFIER && // x
            tokens[2].getType() == TokenType.DECLARATION && // :
            tokens[3].getType() == TokenType.TYPE && // string
            tokens[4].getType() == TokenType.SEMICOLON
    }

    fun isAssignation(tokens: List<Token>): Boolean {
        // there are 2 options for assignation:
        // declaration + assignation OR just declaration
        return (
            tokens[1].getType() == TokenType.IDENTIFIER && // x
                tokens[2].getType() == TokenType.DECLARATION && // :
                tokens[3].getType() == TokenType.TYPE && // string
                tokens[4].getType() == TokenType.ASSIGNATION && // =
                (
                    tokens[5].getType() != TokenType.TYPE && tokens[5].getType() != TokenType.DECLARATION &&
                        tokens[5].getType() != TokenType.ASSIGNATION &&
                        tokens[5].getType() != TokenType.SEMICOLON &&
                        tokens[5].getType() != TokenType.KEYWORD
                ) &&
                tokens[tokens.size - 1].getType() == TokenType.SEMICOLON
        ) ||
            (
                tokens[0].getType() == TokenType.IDENTIFIER &&
                    tokens[1].getType() == TokenType.ASSIGNATION &&
                    tokens[2].getType() == TokenType.VALUE &&
                    tokens[tokens.size - 1].getType() == TokenType.SEMICOLON
            )
    }

    fun isCallingMethod(tokens: List<Token>): Boolean {
        // this would be the calling function structure that comes from lexer:
        // print("hello")
        // sum(a + b)
        return tokens[0].getType() == TokenType.KEYWORD && // println or sum
            tokens[1].getType() == TokenType.PARENTHESIS && // (
            tokens[tokens.size - 2].getType() == TokenType.PARENTHESIS && // )
            tokens[tokens.size - 1].getType() == TokenType.SEMICOLON
    }
}

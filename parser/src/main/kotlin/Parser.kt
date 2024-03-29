import components.*
import components.ast.AST
import components.ast.ASTInterface
import error.ParserError
import kotlin.math.abs

class Parser : ParserInterface {
    private val typeComparator = ComparatorTokenType()
    private val valueComparator = ComparatorTokenValue()
    private val DOESNTMATTERTYPES = listOf(TokenType.PARENTHESIS, TokenType.KEYWORD)
    private val VALUETYPES = listOf(TokenType.INTEGER, TokenType.STRING)
    private val FUNCIONTYPES = listOf(TokenType.FUNCTION, TokenType.PARENTHESIS)
    private val twoChildrenType = listOf(TokenType.OPERATOR, TokenType.ASSIGNATION, TokenType.DECLARATION)

    override fun parse(tokens: List<Token>): ASTInterface {
        if (tokens.last().getType() != TokenType.SEMICOLON)
            throw ParserError("error: ';' expected  " + tokens.last().getPosition(), tokens.last())
        val tokWSemicolon = tokens.subList(0, tokens.size - 1)
        if (checkDeclaration(tokWSemicolon)) return transformDeclaration(tokens)
        else if (isAssignation(tokWSemicolon)) return transformAssignation(tokens)
        else if (isCallingMethod(tokWSemicolon)) return transformFunction(tokens)
        throw ParserError("PrintScript couldn't parse that code " + tokens[0].getPosition(), tokens[0])
    }

    private fun transformDeclaration(tokens: List<Token>): ASTInterface {
        val decl = tokens[2]
        val identifier = tokens[1]
        val value = tokens[3]
        return AST(decl, listOf(getLeaf(identifier), getLeaf(value)))
    }

    private fun transformAssignation(tokens: List<Token>): ASTInterface {
        val assignIndex: Int = findAssignIndex(tokens)
        val ident: ASTInterface = transformIdent(tokens.subList(0, assignIndex))
        val assign = tokens[assignIndex]
        val value = transformValue(tokens.subList(assignIndex + 1, tokens.size))
        return AST(assign, listOf(ident, value))
    }

    private fun transformFunction(tokens: List<Token>): ASTInterface {
        val function = tokens[0]
        val value = transformValue(tokens.subList(1, tokens.size))
        return AST(function, listOf(value))
    }

    private fun transformIdent(tokens: List<Token>): ASTInterface {
        if (tokens.size == 1) return getLeaf(tokens[0])
        return transformDeclaration(tokens)
    }

    private fun findAssignIndex(tokens: List<Token>): Int {
        for (i in tokens.indices)
            if (tokens[i].getType() == TokenType.ASSIGNATION) return i
        return -1
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
    private fun add(token: Token, ast: ASTInterface): ASTInterface {
        if (token.getType() in DOESNTMATTERTYPES) return ast
        if (ast.isEmpty()) return ast.addChildren(getLeaf(token))
        val compareTokens = compareValueAndType(token, ast.getToken())
        if (ast.getChildrenAmount() == 1 && ast.getToken().getType() in twoChildrenType && rootIsBigger(compareTokens))
            return ast.addChildren(getLeaf(token))
        return if (rootIsBigger(compareTokens)) compWChildren(token, ast)
        else if (compareTokens == 1) AST(token, ast)
        else if (abs(compareTokens) == 2) ast
        else ast.addChildren(getLeaf(token))
    }

    private fun compareValueAndType(token: Token, root: Token): Int {
        val compValue = valueComparator.compare(token, root)
        val compToken = typeComparator.compare(token.getType(), root.getType())
        return if (compToken != 0) compToken
        else compValue
    }

    private fun compWChildren(token: Token, ast: ASTInterface): ASTInterface {
        for (child in ast.getChildren()) {
            val comp = compareValueAndType(token, child.getToken())
            when (comp) {
                // child's root bigger than token
                -1 -> return removeLastChild(ast, token)
                // token bigger than child's root
                1 -> return removeLastChild(ast, token)
            }
        }
        return ast.addChildren(getLeaf(token))
    }

    private fun removeLastChild(ast: ASTInterface, token: Token): ASTInterface {
        val lastChild = ast.getChildren().last()
        return ast.replace(lastChild, add(token, lastChild))
    }

    private fun rootIsBigger(compareTokens: Int) = compareTokens == -1

    // this would check if tokens apply to the declaration structure:
    // let x: string;
    private fun checkDeclaration(tokens: List<Token>): Boolean {
        val declarationTypes = getDeclarationTypesAndValueType(tokens)
        if(declarationTypes.isEmpty()) return false

        val tokenTypes = tokens.map { it.getType() }
        val declarationTypesPresent = declarationTypes.intersect(tokenTypes.toSet())
        if (declarationTypesPresent.size > 2 && declarationTypesPresent == tokenTypes)
            throw ParserError(
                "error: to declare a variable, it's expected to do it by 'let <name of the variable>: <type of the variable>'",
                tokens[0]
            )

        return tokenTypes == declarationTypes
    }

    private fun getDeclarationTypesAndValueType(tokens: List<Token>): List<TokenType> {
        val declarationTypes =
            mutableListOf(TokenType.KEYWORD, TokenType.IDENTIFIER, TokenType.DECLARATION)

        if (tokens.size == declarationTypes.size + 1) {
            for (value in VALUETYPES)
                if (tokens[tokens.size - 1].getType() == value) {
                    declarationTypes.add(value)
                    return declarationTypes
                }
        }
        val tokenTypes = tokens.map { it.getType() }
        if (tokenTypes == declarationTypes) throw ParserError(
            "error: expected variable type. " +
                    "Remember to declare a variable, it's expected to do it by 'let <name of the variable>: <type of the variable>'",
            tokens.last()
        )
        return emptyList()
    }

    private fun isAssignation(tokens: List<Token>): Boolean {
        // there are 2 options for assignation:
        // declaration + assignation OR just declaration
        if(tokens.size < 3) return false
        val assignIndex = findAssignIndex(tokens)
        if (assignIndex == -1) return false
        if (checkDeclaration(tokens.subList(0, assignIndex)) || checkIdentifier(tokens.subList(0, assignIndex)))
            return checkValue(tokens.subList(assignIndex+1, tokens.size))
        return false
    }

    private fun checkIdentifier(tokens: List<Token>): Boolean {
        return if (tokens.size != 1) false
        else tokens[0].getType() == TokenType.IDENTIFIER
    }

    private fun isCallingMethod(tokens: List<Token>): Boolean {
        // this would be the calling function structure that comes from lexer:
        // print("hello")
        // sum(a + b)
        val tokenTypes = tokens.map { it.getType() }
        if (tokenTypes.subList(0, FUNCIONTYPES.size) == FUNCIONTYPES) {
            if (tokenTypes.last() != TokenType.PARENTHESIS) throw ParserError(
                tokens.last().getPosition().startLine.toString() + ":error: ')' expected " + tokens.last().getPosition(),
                tokens.last()
            )
            return checkValue(tokens)
        }
        return false
    }

    private fun checkValue(tokens: List<Token>): Boolean {
        val invalidValueTypes =
            listOf(TokenType.INTEGER, TokenType.STRING, TokenType.DECLARATION, TokenType.ASSIGNATION, TokenType.SEMICOLON, TokenType.KEYWORD)
        tokens
            .filter { it.getType() in invalidValueTypes }
            .forEach { throw ParserError("Invalid value type ${it.getPosition()}", it)}
        return true
    }

}
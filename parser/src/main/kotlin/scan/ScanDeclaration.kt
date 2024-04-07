package scan

import components.Position
import components.Token
import components.TokenType
import components.statement.*
import error.ParserError

class ScanDeclaration : ScanStatement {
    private val declarationTypes = listOf(TokenType.KEYWORD, TokenType.IDENTIFIER, TokenType.DECLARATION, TokenType.TYPE)

    override fun canHandle(tokens: List<Token>): Boolean {
        return if (declarationTypes == getTokenTypes(tokens)) {
            true
        } else {
            checkIfDeclarationTypesMissing(tokens)
        }
    }

    override fun makeAST(tokens: List<Token>): Statement {
        val keyword: Keyword = getKeyword(tokens[0])
        val variable: Variable = getVariable(tokens[1])
        val declPosition: Position = getPosition(tokens[2])
        val type: Type = getType(tokens[3])
        return Declaration(keyword, variable, type, declPosition)
    }

    private fun checkCollections(
        declarationTypesPresent: Set<TokenType>,
        tokenTypes: List<TokenType>,
    ): Boolean {
        if (tokenTypes.size != declarationTypesPresent.size) return false
        for ((i, type) in declarationTypesPresent.withIndex()) {
            if (i >= tokenTypes.size) return false
            if (tokenTypes[i] != type) return false
        }
        return true
    }

    private fun checkIfDeclarationTypesMissing(tokens: List<Token>): Boolean {
        val declarationTypesPresent = declarationTypes.intersect(getTokenTypes(tokens).toSet())
        if (checkIfDeclarationTypesMissing(declarationTypesPresent, tokens)) {
            throw ParserError(
                "error: to declare a variable, it's expected to do it by 'let <name of the variable>: <type of the variable>'",
                tokens[0],
            )
        }

        return false
    }

    private fun checkIfDeclarationTypesMissing(
        declarationTypesPresent: Set<TokenType>,
        tokens: List<Token>,
    ) = declarationTypesPresent.size > 2 && checkCollections(declarationTypesPresent, getTokenTypes(tokens))

    private fun getTokenTypes(tokens: List<Token>): List<TokenType> = tokens.map { it.getType() }

    private fun getKeyword(token: Token): Keyword  {
        val modifier: Modifier = Modifier.MUTABLE
        if (token.getValue() != "let") throw ParserError("error: keyword not found", token)
        return Keyword(modifier, token.getValue(), token.getPosition())
    }

    private fun getType(token: Token): Type = Type(token.getValue(), token.getPosition())

    private fun getPosition(token: Token): Position = token.getPosition()

    private fun getVariable(token: Token): Variable = Variable(token.getValue(), token.getPosition())
}

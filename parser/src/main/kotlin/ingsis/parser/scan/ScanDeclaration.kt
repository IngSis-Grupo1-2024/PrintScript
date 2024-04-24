package ingsis.parser.scan

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.parser.error.ParserError

object PSScanDeclaration {
    fun createScanDeclaration(version: String): ScanDeclaration {
        return when (version) {
            "VERSION_1" -> ScanDeclaration(listOf("let"), emptyList(), variableTypes(version))
            "VERSION_2" -> ScanDeclaration(listOf("let"), listOf("const"), variableTypes(version))
            else -> ScanDeclaration(listOf("let"), emptyList(), variableTypes(version))
        }
    }

    private fun variableTypes(version: String): Map<String, TokenType> {
        return when (version) {
            "VERSION_1" -> mapOf("string" to TokenType.STRING, "number" to TokenType.INTEGER)
            "VERSION_2" -> variableTypes("VERSION_1") + mapOf("boolean" to TokenType.BOOLEAN)
            else -> variableTypes("VERSION_1")
        }
    }
}

class ScanDeclaration(
    private val mutableKeywords: List<String>,
    private val immutableKeywords: List<String>,
    private val variableTypes: Map<String, TokenType>,
) : ScanStatement {
    private val declarationTypes = listOf(TokenType.KEYWORD, TokenType.IDENTIFIER, TokenType.DECLARATION, TokenType.TYPE)

    override fun canHandle(tokens: List<Token>): Boolean {
        if (checkIfThereIsNoDelimiter(tokens)) {
            throw ParserError("error: delimiter (;) expected at " + tokens.last().getPosition(), tokens.last())
        }

        val tokWODelimiter = tokens.subList(0, tokens.size - 1)
        return canHandleWODelimiter(tokWODelimiter)
    }

    override fun canHandleWODelimiter(tokens: List<Token>): Boolean =
        if (declarationTypes == getTokenTypes(tokens)) {
            true
        } else {
            checkMissingTypes(tokens)
        }

    override fun makeAST(tokens: List<Token>): Statement {
        val keyword: Keyword = getKeyword(tokens[0])
        val variable: Variable = getVariable(tokens[1])
        val declPosition: Position = getPosition(tokens[2])
        val type: Type = getType(tokens[3])
        return Declaration(keyword, variable, type, declPosition)
    }

    private fun checkMissingTypes(tokens: List<Token>): Boolean {
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
    ) = declarationTypesPresent.size >= 2 && tokens.size == declarationTypesPresent.size

    private fun getTokenTypes(tokens: List<Token>): List<TokenType> = tokens.map { it.getType() }

    private fun getKeyword(token: Token): Keyword {
        val modifier: Modifier =
            if (token.getValue() in mutableKeywords) {
                Modifier.MUTABLE
            } else if (token.getValue() in immutableKeywords) {
                Modifier.IMMUTABLE
            } else {
                throw ParserError("error: keyword not found", token)
            }
        return Keyword(modifier, token.getValue(), token.getPosition())
    }

    private fun getType(token: Token): Type {
        val tokenType =
            when (token.getValue()) {
                in variableTypes.keys -> variableTypes[token.getValue()]!!
                else -> throw ParserError("error: invalid token", token)
            }
        return Type(tokenType, token.getPosition())
    }

    private fun getPosition(token: Token): Position = token.getPosition()

    private fun getVariable(token: Token): Variable = Variable(token.getValue(), token.getPosition())

    private fun checkIfThereIsNoDelimiter(tokens: List<Token>) = tokens.last().getType() != TokenType.DELIMITER
}

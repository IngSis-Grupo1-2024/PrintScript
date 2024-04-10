package scan

import components.Position
import components.Token
import components.TokenType
import components.statement.PrintLine
import components.statement.Statement
import error.ParserError

class ScanPrintLine : ScanStatement {
    private val scanValue = ScanValue()
    private val functionTypes = listOf(TokenType.FUNCTION, TokenType.PARENTHESIS)

    override fun canHandle(tokens: List<Token>): Boolean {
        val tokenTypes = getTokenTypes(tokens)

        if (hasFunctionTypes(tokenTypes)) {
            if (hasLastParenthesis(tokenTypes)) {
                throw ParserError(
                    ":error: ')' expected.",
                    tokens.last(),
                )
            } else if (emptyValue(tokens)) {
                throw ParserError("error: expected value", tokens.last())
            }
            return valueCanHandle(tokens)
        }
        return false
    }

    private fun lastPosition(tokens: List<Token>): Position = tokens.last().getPosition()

    private fun valueCanHandle(tokens: List<Token>) = scanValue.canHandle(tokens.subList(functionTypes.size, tokens.size - 1))

    private fun hasLastParenthesis(tokenTypes: List<TokenType>) = tokenTypes.last() != TokenType.PARENTHESIS

    private fun hasFunctionTypes(tokenTypes: List<TokenType>) = tokenTypes.subList(0, functionTypes.size) == functionTypes

    private fun getTokenTypes(tokens: List<Token>): List<TokenType> = tokens.map { it.getType() }

    private fun emptyValue(tokens: List<Token>): Boolean {
        val value = tokens.subList(functionTypes.size, tokens.size)
        return value.isEmpty()
    }

    override fun makeAST(tokens: List<Token>): Statement {
        val printLinePosition = tokens[0].getPosition()
        val value = scanValue.makeValue(tokens.subList(2, tokens.size - 1))
        return PrintLine(printLinePosition, value)
    }
}

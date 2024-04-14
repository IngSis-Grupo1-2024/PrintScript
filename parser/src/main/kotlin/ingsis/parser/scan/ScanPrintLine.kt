package ingsis.parser.scan

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.PrintLine
import ingsis.components.statement.Statement
import ingsis.parser.error.ParserError

class ScanPrintLine : ScanStatement {
    private val scanValue = ScanValue()
    private val functionTypes = listOf(TokenType.FUNCTION, TokenType.PARENTHESIS)

    override fun canHandle(tokens: List<Token>): Boolean {
        if (checkIfThereIsNoDelimiter(tokens)) {
            throw ParserError("error: ';' expected  " + tokens.last().getPosition(), tokens.last())
        }

        val tokensWODelimiter = tokens.subList(0, tokens.size - 1)
        return canHandleWODelimiter(tokensWODelimiter)
    }

    override fun canHandleWODelimiter(tokens: List<Token>): Boolean {
        if(tokens.size < functionTypes.size + 1) return false
        val tokenTypes = getTokenTypes(tokens)
        if (hasFunctionTypes(tokenTypes)) {
            if (!hasLastParenthesis(tokens)) {
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

    override fun makeAST(tokens: List<Token>): Statement {
        val printLinePosition = tokens[0].getPosition()
        val value = scanValue.makeValue(tokens.subList(2, tokens.size - 2))
        return PrintLine(printLinePosition, value)
    }

    private fun lastPosition(tokens: List<Token>): Position = tokens.last().getPosition()

    private fun valueCanHandle(tokens: List<Token>) = scanValue.canHandle(tokens.subList(functionTypes.size, tokens.size - 1))

    private fun hasLastParenthesis(tokenTypes: List<Token>) =
        tokenTypes.last().getType() == TokenType.PARENTHESIS && tokenTypes.last().getValue() == ")"

    private fun hasFunctionTypes(tokenTypes: List<TokenType>) = tokenTypes.subList(0, functionTypes.size) == functionTypes

    private fun getTokenTypes(tokens: List<Token>): List<TokenType> = tokens.map { it.getType() }

    private fun emptyValue(tokens: List<Token>): Boolean {
        val value = tokens.subList(functionTypes.size, tokens.size)
        return value.isEmpty()
    }

    private fun checkIfThereIsNoDelimiter(tokens: List<Token>) = tokens.last().getType() != TokenType.DELIMITER
}

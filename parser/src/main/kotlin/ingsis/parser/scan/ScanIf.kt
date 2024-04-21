package ingsis.parser.scan

import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.Else
import ingsis.components.statement.If
import ingsis.components.statement.SingleValue
import ingsis.components.statement.Statement
import ingsis.parser.error.ParserError

object PSScanIf {
    fun createIf(version: String): ScanIf {
        return ScanIf(scanValue(version))
    }

    private fun scanValue(version: String) = PrintScriptScanValue.createScanValue(version)
}

class ScanIf(private val scanValue: ScanValue) : ScanStatement {
    private val typesLastPart = listOf(TokenType.PARENTHESIS, TokenType.BRACES)
    private val valuesLastPart = listOf(")", "{")
    private val isInsideIf = false

    override fun canHandle(tokens: List<Token>): Boolean {
        if (!isInsideIf) return checkTypes(tokens)
        return false
    }

    private fun checkTypes(tokens: List<Token>): Boolean {
        if (tokens.size < 5) return false
        if (checkIf(tokens)) {
            if (!checkOpeningParenthesis(tokens)) {
                throw ParserError("error: each if statement must be continue with an '('", tokens[1])
            }
            if (checkLastPart(tokens)) {
                return scanValue.canHandle(tokens.subList(2, tokens.size - typesLastPart.size))
            }
            throw ParserError("error: each if statement must be if(condition){", tokens.last())
        }
        return false
    }

    override fun canHandleWODelimiter(tokens: List<Token>): Boolean = canHandle(tokens)

    override fun makeAST(tokens: List<Token>): Statement {
        return If(
            SingleValue(tokens[2]),
            Else(emptyList()),
            emptyList(),
        )
    }

    private fun checkIf(tokens: List<Token>): Boolean = tokens[0].getValue() == "if" && tokens[0].getType() == TokenType.FUNCTION_KEYWORD

    private fun checkOpeningParenthesis(tokens: List<Token>) = tokens[1].getValue() == "(" && tokens[1].getType() == TokenType.PARENTHESIS

    private fun checkLastPart(tokens: List<Token>) =
        getLastPartOfTokenTypes(tokens) == typesLastPart &&
            getLastPartOfTokenValues(tokens) == valuesLastPart

    private fun getLastPartOfTokenTypes(tokens: List<Token>) =
        tokens.subList(tokens.size - typesLastPart.size, tokens.size).map { it.getType() }

    private fun getLastPartOfTokenValues(tokens: List<Token>) =
        tokens.subList(tokens.size - typesLastPart.size, tokens.size).map { it.getValue() }
}

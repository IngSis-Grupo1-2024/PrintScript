package ingsis.parser.scan

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.Operator
import ingsis.components.statement.SingleValue
import ingsis.components.statement.Value
import ingsis.parser.error.ParserError

object PSScanFunction {
    fun createScanFunction(version: String): ScanFunction {
        return ScanFunction(scanValue(version))
    }

    private fun scanValue(version: String) = PrintScriptScanValue.createScanValue(version)
}

class ScanFunction(private val scanValue: ScanValue) : ValueScanner {
    override fun canHandle(tokens: List<Token>): Boolean {
        if (tokens.size < 3) return false
        if (checkFirstPart(tokens)) {
            if (!hasParenthesis(tokens[1], "(")) {
                throw ParserError(
                    "error: each function call " +
                        "must continue with an opening parenthesis",
                    tokens[1],
                )
            }
            if (!hasParenthesis(tokens.last(), ")")) {
                throw ParserError(
                    "error: each function call " +
                        "must finished with an closed parenthesis",
                    tokens.last(),
                )
            }
            return scanValue.canHandle(tokens.subList(2, tokens.size - 1))
        }
        return false
    }

    override fun makeValue(tokens: List<Token>): Value {
        val value = scanValue.makeValue(tokens.subList(2, tokens.size - 1))
        if (value.isEmpty()) return SingleValue(Token(Position(), "", TokenType.STRING))
        return Operator(tokens[0], value)
    }

    private fun checkFirstPart(tokens: List<Token>): Boolean {
        return tokens[0].getType() == TokenType.FUNCTION &&
            (isReadEnv(tokens[0]) || isReadInput(tokens[0]))
    }

    private fun hasParenthesis(
        token: Token,
        value: String,
    ): Boolean = token.getType() == TokenType.PARENTHESIS && token.getValue() == value

    private fun isReadInput(token: Token): Boolean = token.getValue() == "readInput"

    private fun isReadEnv(token: Token): Boolean = token.getValue() == "readEnv"
}

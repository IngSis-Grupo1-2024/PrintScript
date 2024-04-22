package ingsis.parser.scan

import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.parser.PrintScriptParser
import ingsis.parser.error.ParserError

object PSScanIf {
    fun createIf(version: String): ScanIf {
        return ScanIf(scanValue(version), version)
    }

    private fun scanValue(version: String) = PrintScriptScanValue.createScanValue(version)
}

class ScanIf(private val scanValue: ScanValue, private val version: String) : ScanStatement {
    private val typesLastPart = listOf(TokenType.PARENTHESIS, TokenType.BRACES)
    private val valuesLastPart = listOf(")", "{")
    private var isInsideIf = false
    private val statements = mutableListOf<Statement>()
    private var comparison: Value = EmptyValue()

    override fun canHandle(tokens: List<Token>): Boolean {
        return if (isInsideIf) {
            true
        } else {
            checkTypes(tokens)
        }
    }

    override fun canHandleWODelimiter(tokens: List<Token>): Boolean = canHandle(tokens)

    override fun makeAST(tokens: List<Token>): Statement? {
        if (!isInsideIf) {
            comparison = scanValue.makeValue(tokens.subList(2, tokens.size - typesLastPart.size))
            isInsideIf = true
        } else if (checkLastBrace(tokens)) {
            addStatement(tokens)
            if (statements.isEmpty()) throw ParserError("error: there are no statements inside if", tokens.last())
            return getStatementAndResetInternalCollaborators()
        } else {
            addStatement(tokens)
        }
        return null
    }

    private fun getStatementAndResetInternalCollaborators(): If {
        val statement = getStatement()
        resetInternalCollaborators()
        return statement
    }

    private fun getStatement() =
        If(
            comparison,
            Else(emptyList()),
            statements.toList(),
        )

    private fun resetInternalCollaborators() {
        statements.clear()
        isInsideIf = false
        comparison = EmptyValue()
    }

    private fun addStatement(tokens: List<Token>) {
        if (!checkLastBrace(tokens)) {
            val parser = PrintScriptParser.createParser(version)
            val statement = parser.parse(tokens)
            statement.forEach{
                if (it != null) {
                    statements.add(it)
                }
            }
        } else if (tokens.size > 1) {
            val parser = PrintScriptParser.createParser(version)
            val statement = parser.parse(tokens.subList(0, tokens.size - 1))
            statement.forEach{
                if (it != null) {
                    statements.add(it)
                }
            }
        }
    }

    private fun checkLastBrace(tokens: List<Token>) = tokens.last().getType() == TokenType.BRACES && tokens.last().getValue() == "}"

    private fun checkIf(tokens: List<Token>): Boolean = tokens[0].getValue() == "if" && tokens[0].getType() == TokenType.FUNCTION_KEYWORD

    private fun checkTypes(tokens: List<Token>): Boolean {
        if (tokens.size < 5) return false
        if (checkIf(tokens)) {
            if (!checkOpeningParenthesis(tokens)) {
                throw ParserError("error: each if statement must continue with an '('", tokens[1])
            }
            if (checkLastPart(tokens)) {
                return checkValue(tokens)
            }
            throw ParserError("error: each if statement must be if(condition){", tokens.last())
        }
        return false
    }

    private fun checkValue(tokens: List<Token>) = scanValue.canHandle(tokens.subList(2, tokens.size - typesLastPart.size))

    private fun checkOpeningParenthesis(tokens: List<Token>) = tokens[1].getValue() == "(" && tokens[1].getType() == TokenType.PARENTHESIS

    private fun checkLastPart(tokens: List<Token>) =
        getLastPartOfTokenTypes(tokens) == typesLastPart &&
            getLastPartOfTokenValues(tokens) == valuesLastPart

    private fun getLastPartOfTokenTypes(tokens: List<Token>) =
        tokens.subList(tokens.size - typesLastPart.size, tokens.size).map { it.getType() }

    private fun getLastPartOfTokenValues(tokens: List<Token>) =
        tokens.subList(tokens.size - typesLastPart.size, tokens.size).map { it.getValue() }
}

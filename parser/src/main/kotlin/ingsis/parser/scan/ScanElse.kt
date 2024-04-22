package ingsis.parser.scan

import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.Else
import ingsis.components.statement.Statement
import ingsis.parser.PrintScriptParser
import ingsis.parser.error.ParserError

object PSScanElse {
    fun createScanElse(version: String): ScanElse = ScanElse(version)
}

class ScanElse(private val version: String) : ScanStatement {
    private var isInsideElse = false
    private val statements = mutableListOf<Statement>()

    override fun canHandle(tokens: List<Token>): Boolean {
        return if (isInsideElse) {
            true
        } else {
            checkElseTypes(tokens)
        }
    }

    override fun canHandleWODelimiter(tokens: List<Token>): Boolean = canHandle(tokens)

    override fun makeAST(tokens: List<Token>): Statement? {
        if (!isInsideElse) {
            isInsideElse = true
        } else if (checkLastBrace(tokens)) {
            addStatement(tokens)
            if (statements.isEmpty()) throw ParserError("error: there are no statements inside else", tokens.last())
            return getStatementAndResetInternalCollaborators()
        } else {
            addStatement(tokens)
        }
        return null
    }

    private fun getStatementAndResetInternalCollaborators(): Else {
        val statement = getStatement()
        resetInternalCollaborators()
        return statement
    }

    private fun getStatement() = Else(statements.toList())

    private fun resetInternalCollaborators() {
        statements.clear()
        isInsideElse = false
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

    private fun checkElseTypes(tokens: List<Token>): Boolean {
        if (checkElse(tokens)) {
            if (!checkOpenBraces(tokens)) {
                throw ParserError("error: each else statement must continue with an '{'", tokens[1])
            }
            return true
        }
        return false
    }

    private fun checkElse(tokens: List<Token>) = tokens[0].getType() == TokenType.FUNCTION_KEYWORD && tokens[0].getValue() == "else"

    private fun checkOpenBraces(tokens: List<Token>): Boolean = tokens[1].getValue() == "{" && tokens[1].getType() == TokenType.BRACES
}

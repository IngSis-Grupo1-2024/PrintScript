package ingsis.parser

import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.Statement
import ingsis.parser.error.ParserError
import ingsis.parser.scan.*

object PrintScriptParser {
    fun createParser(version: String): Parser {
        return when (version) {
            "VERSION_1" -> Parser(listOf(scanDeclaration(version), scanAssignation(version), ScanPrintLine()))
            "VERSION_2" -> Parser(listOf(scanDeclaration(version), scanAssignation(version), ScanPrintLine()))
            else -> Parser(listOf(scanDeclaration(version), scanAssignation(version), ScanPrintLine()))
        }
    }

    private fun scanDeclaration(version: String) = PSScanDeclaration.createScanDeclaration(version)

    private fun scanAssignation(version: String) = PSScanAssignation.createScanAssignation(version)
}

class Parser(private val scanStatement: List<ScanStatement>) {
    fun parse(tokensWSymbols: List<Token>): Statement {
        val tokens: List<Token> = changeSymbolType(tokensWSymbols)

        scanStatement.forEach {
            if (it.canHandle(tokens)) return it.makeAST(tokens)
        }

        throw ParserError("PrintScript couldn't parse that code " + tokens[0].getPosition(), tokens[0])
    }

    private fun changeSymbolType(tokens: List<Token>): List<Token> =
        tokens.map { token ->
            if (isSymbol(token)) {
                getTokenWithRightType(token)
            } else {
                token
            }
        }

    private fun isSymbol(token: Token) = token.getType() == TokenType.SYMBOL

    private fun getTokenWithRightType(token: Token): Token {
        if (checkIfString(token) || checkIfChar(token)) {
            return Token(token.getPosition(), getStringWithoutQuotes(token), TokenType.STRING)
        } else if (checkIfDouble(token)) {
            return Token(token.getPosition(), token.getValue(), TokenType.DOUBLE)
        } else if (checkIfNumber(token)) {
            return Token(token.getPosition(), token.getValue(), TokenType.INTEGER)
        }
        return Token(token.getPosition(), token.getValue(), TokenType.IDENTIFIER)
    }

    private fun getStringWithoutQuotes(token: Token): String = token.getValue().substring(1, token.getValue().length - 1)

    private fun checkIfNumber(token: Token): Boolean = token.getValue().toIntOrNull() != null

    private fun checkIfDouble(token: Token): Boolean = token.getValue().contains(".") && (token.getValue().toDoubleOrNull() != null)

    private fun checkIfString(token: Token) = token.getValue()[0] == '"' && token.getValue().last() == '"'

    private fun checkIfChar(token: Token): Boolean = token.getValue()[0] == '\'' && token.getValue().last() == '\''
}

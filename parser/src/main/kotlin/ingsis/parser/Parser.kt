package ingsis.parser

import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.Statement
import ingsis.parser.error.ParserError
import ingsis.parser.scan.*
import ingsis.parser.symbolType.*

object PrintScriptParser {
    fun createParser(version: String): Parser {
        return when (version) {
            "VERSION_1" -> Parser(getScannersOfV1(version), getSymbolChangersV1())
            "VERSION_2" -> Parser(getScannersOfV1(version), getSymbolChangersV2())
            else -> Parser(getScannersOfV1(version), getSymbolChangersV1())
        }
    }

    private fun getSymbolChangersV1(): List<SymbolChanger> =
        listOf(
            StringSymbolChanger(), DoubleSymbolChanger(),
            IntegerSymbolChanger(), IdentifierSymbolChanger()
        )

    private fun getScannersOfV1(version: String) =
        listOf(scanDeclaration(version), scanAssignation(version), scanPrintLine(version))

    private fun scanPrintLine(version: String) = PSScanPrintLine.createPrintLine(version)

    private fun getSymbolChangersV2(): List<SymbolChanger> =
        listOf(
            StringSymbolChanger(), DoubleSymbolChanger(),
            IntegerSymbolChanger(), BooleanSymbolChanger(),
            IdentifierSymbolChanger())

    private fun scanDeclaration(version: String) = PSScanDeclaration.createScanDeclaration(version)

    private fun scanAssignation(version: String) = PSScanAssignation.createScanAssignation(version)
}

class Parser(
    private val scanStatement: List<ScanStatement>,
    private val symbolChangers: List<SymbolChanger>
) {
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
        symbolChangers.forEach {
            if (it.canHandle(token)) return it.changeToken(token)
        }
        throw ParserError("error: There is no type for: " + token.getValue(), token)
    }

}

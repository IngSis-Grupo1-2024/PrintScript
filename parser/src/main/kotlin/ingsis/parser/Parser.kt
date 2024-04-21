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
            "VERSION_2" -> Parser(getScannersOfV2(version), getSymbolChangersV2())
            else -> Parser(getScannersOfV1(version), getSymbolChangersV1())
        }
    }

    private fun getSymbolChangersV1(): List<SymbolChanger> =
        listOf(
            StringSymbolChanger(),
            DoubleSymbolChanger(),
            IntegerSymbolChanger(),
            IdentifierSymbolChanger(),
        )

    private fun getScannersOfV1(version: String) = listOf(scanDeclaration(version), scanAssignation(version), scanPrintLine(version))

    private fun getScannersOfV2(version: String): List<ScanStatement> =
        getScannersOfV1(version) + listOf(scanIf(version), scanElse(version))

    private fun scanElse(version: String) = PSScanElse.createScanElse(version)

    private fun scanPrintLine(version: String) = PSScanPrintLine.createPrintLine(version)

    private fun getSymbolChangersV2(): List<SymbolChanger> =
        listOf(
            StringSymbolChanger(),
            DoubleSymbolChanger(),
            IntegerSymbolChanger(),
            BooleanSymbolChanger(),
            IdentifierSymbolChanger(),
        )

    private fun scanDeclaration(version: String) = PSScanDeclaration.createScanDeclaration(version)

    private fun scanAssignation(version: String) = PSScanAssignation.createScanAssignation(version)

    private fun scanIf(version: String) = PSScanIf.createIf(version)
}

class Parser(
    private val scanStatement: List<ScanStatement>,
    private val symbolChangers: List<SymbolChanger>,
) {
    private val ifIndex: Int = findIfIndex()
    private val elseIndex: Int = findElseIndex()

    fun parse(tokensWSymbols: List<Token>): Statement? {
        val tokens: List<Token> = changeSymbolType(tokensWSymbols)

        if (ifCanHandle(tokens)) return ifMakeAst(tokens)

        if (elseCanHandle(tokens)) {
            return elseMakeAst(tokens)
        } else {
            scanStatement.forEach {
                if (it.canHandle(tokens)) return it.makeAST(tokens)
            }
        }

        throw ParserError("PrintScript couldn't parse that code.", tokens[0])
    }

    private fun ifMakeAst(tokens: List<Token>): Statement? = scanStatement[ifIndex].makeAST(tokens)

    private fun elseMakeAst(tokens: List<Token>): Statement? = scanStatement[elseIndex].makeAST(tokens)

    private fun ifCanHandle(tokens: List<Token>): Boolean =
        if (checkIfIndex()) {
            scanStatement[ifIndex].canHandle(tokens)
        } else {
            false
        }

    private fun elseCanHandle(tokens: List<Token>): Boolean =
        if (checkElseIndex()) {
            scanStatement[elseIndex].canHandle(tokens)
        } else {
            false
        }

    private fun checkIfIndex(): Boolean = ifIndex != -1

    private fun checkElseIndex(): Boolean = elseIndex != -1

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

    private fun findIfIndex(): Int {
        scanStatement.indices.forEach {
            if (scanStatement[it] is ScanIf) return it
        }
        return -1
    }

    private fun findElseIndex(): Int {
        scanStatement.indices.forEach {
            if (scanStatement[it] is ScanElse) return it
        }
        return -1
    }
}

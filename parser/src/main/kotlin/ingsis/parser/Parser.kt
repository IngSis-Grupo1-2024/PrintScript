package ingsis.parser

import components.Token
import components.statement.Statement
import ingsis.parser.error.ParserError
import ingsis.parser.scan.ScanAssignation
import ingsis.parser.scan.ScanDeclaration
import ingsis.parser.scan.ScanPrintLine
import ingsis.parser.scan.ScanStatement

object PrintScriptParser {
    fun createParser(version: String): Parser {
        return when (version) {
            "VERSION_1" -> Parser(listOf(ScanDeclaration(), ScanAssignation(), ScanPrintLine()))
            else -> Parser(listOf(ScanDeclaration(), ScanAssignation(), ScanPrintLine()))
        }
    }
}

class Parser(private val scanStatement: List<ScanStatement>) {
    fun parse(tokens: List<Token>): Statement {
        scanStatement.forEach {
            if (it.canHandle(tokens)) return it.makeAST(tokens)
        }

        throw ParserError("PrintScript couldn't parse that code " + tokens[0].getPosition(), tokens[0])
    }
}

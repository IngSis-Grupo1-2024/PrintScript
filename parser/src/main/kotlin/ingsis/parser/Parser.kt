package ingsis.parser

import components.Token
import components.TokenType
import components.statement.Statement
import error.ParserError
import scan.ScanStatement

class Parser(private val scanStatement: List<ScanStatement>) {
    fun parse(tokens: List<Token>): Statement {
        if (checkIfThereIsADelimiter(tokens)) {
            throw ParserError("error: ';' expected  " + tokens.last().getPosition(), tokens.last())
        }

        val tokWOSemicolon = tokens.subList(0, tokens.size - 1)

        scanStatement.forEach {
            if (it.canHandle(tokWOSemicolon)) return it.makeAST(tokWOSemicolon)
        }

        throw ParserError("PrintScript couldn't parse that code " + tokens[0].getPosition(), tokens[0])
    }

    private fun checkIfThereIsADelimiter(tokens: List<Token>) = tokens.last().getType() != TokenType.SEMICOLON
}

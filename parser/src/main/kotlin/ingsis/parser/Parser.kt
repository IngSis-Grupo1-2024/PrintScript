package ingsis.parser

import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.Else
import ingsis.components.statement.EmptyValue
import ingsis.components.statement.If
import ingsis.components.statement.Statement
import ingsis.parser.error.ParserError
import ingsis.parser.scan.*
import ingsis.parser.symbolType.*

class Parser(
    private val scanStatement: List<ScanStatement>,
    private val symbolChangers: List<SymbolChanger>,
) {
    private val ifIndex: Int = findIfIndex()
    private val elseIndex: Int = findElseIndex()
    private var mayHaveContinuousElse = false
    private var ifStatement: If = If(EmptyValue(), Else(emptyList()), emptyList())

    fun parse(tokensWSymbols: List<Token>): List<Statement?> {
        val tokens: List<Token> = changeSymbolType(tokensWSymbols)

        if (ifCanHandle(tokens)) {
            ifMakeAst(tokens)
            return emptyList()
        }

        if (elseCanHandle(tokens)) {
            val statement = elseMakeAst(tokens)
            if (statement != null) {
                if (mayHaveContinuousElse) {
                    ifStatement.addElse(statement as Else)
                    mayHaveContinuousElse = false
                } else {
                    throw ParserError("You cannot implement an else statement without an if", tokens[0])
                }
                return listOf(ifStatement)
            }
            return emptyList()
        } else {
            val statement = scanStatementWOIfAndElse(tokens)

            if (statement == null) {
                throw ParserError("PrintScript couldn't parse that code.", tokens[0])
            } else if (mayHaveContinuousElse) {
                mayHaveContinuousElse = false
                return listOf(ifStatement, statement)
            }

            return listOf(statement)
        }
    }

    fun getIfStatement() = ifStatement

    private fun scanStatementWOIfAndElse(tokens: List<Token>): Statement? {
        if (!checkElseIndex() && !checkIfIndex()) {
            scanStatement.forEach {
                if (it.canHandle(tokens)) return it.makeAST(tokens)
            }
        } else {
            for (index in scanStatement.indices) {
                if (index == elseIndex || index == ifIndex) continue
                if (scanStatement[index].canHandle(tokens)) return scanStatement[index].makeAST(tokens)
            }
        }
        return null
    }

    private fun ifMakeAst(tokens: List<Token>) {
        val statement = scanStatement[ifIndex].makeAST(tokens)
        if (statement != null) {
            ifStatement = statement as If
            mayHaveContinuousElse = true
        }
    }

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

package scan

import components.Token
import components.TokenType
import components.ast.ASTInterface
import components.statement.Statement
import error.ParserError

class ScanValue: ScanStatement {
    private val valueTypes = listOf(TokenType.INTEGER, TokenType.STRING, TokenType.IDENTIFIER)

    override fun canHandle(tokens: List<Token>): Boolean {
        try {
            val invalidValueTypes =
                listOf(TokenType.TYPE, TokenType.DECLARATION, TokenType.ASSIGNATION, TokenType.SEMICOLON, TokenType.KEYWORD)

            tokens
                .filter { it.getType() !in invalidValueTypes }
                .forEach { throw ParserError("Invalid value type ${it.getPosition()}", it) }

            return checkQtyOperatorsAndValues(getNumberOfOpp(tokens), getNumberOfValue(tokens), tokens.last())
        } catch (e: NoSuchElementException) {
            throw ParserError("error: expected value", tokens.last())
        }
    }

    override fun makeAST(tokens: List<Token>): Statement {
        TODO("Not yet implemented")
    }

    private fun checkQtyOperatorsAndValues(numberOfOpp: Int, numberOfValue: Int, lastToken: Token): Boolean {
        if (numberOfValue == numberOfOpp + 1) return true
        if (numberOfOpp == 0 && numberOfValue == 0) throw ParserError("error: expected value", lastToken)
        throw ParserError("error: wrong number of values and operators", lastToken)
    }

    private fun getNumberOfValue(tokens: List<Token>) = tokens.filter { it.getType() in valueTypes }.size

    private fun getNumberOfOpp(tokens: List<Token>) =
        tokens.filter { it.getType() == TokenType.OPERATOR }.size
}
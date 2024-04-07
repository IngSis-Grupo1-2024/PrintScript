package scan

import components.Token
import components.TokenType
import components.ast.AST
import components.ast.ASTInterface
import components.statement.*
import error.ParserError
import javax.swing.plaf.basic.BasicGraphicsUtils

class ScanAssignation : ScanStatement {
    private val scanDeclaration = ScanDeclaration()
    private val scanValue = ScanValue()

    override fun canHandle(tokens: List<Token>): Boolean {
        if (tokens.size < 3) return false
        val assignIndex = findAssignIndex(tokens)
        if (assignIndex == -1) return false
        if (checkFirstPartOfAssignation(tokens, assignIndex)){
            if(emptyValue(tokens, assignIndex)) throw ParserError("error: expected value", tokens[assignIndex])
            return scanValue.canHandle(tokens.subList(assignIndex + 1, tokens.size))
        }
        return false
    }

    private fun emptyValue(tokens: List<Token>, assignIndex: Int): Boolean {
        val value = tokens.subList(assignIndex+1, tokens.size)
        return value.isEmpty()
    }

    override fun makeAST(tokens: List<Token>): Statement {
        val assignIndex: Int = findAssignIndex(tokens)
        if (checkIfCompound(tokens, assignIndex)) return compoundAssignation(tokens, assignIndex)
        return simpleAssignation(tokens, assignIndex)
    }

    private fun compoundAssignation(tokens: List<Token>, assignIndex: Int): Statement {
        val decl : Declaration = scanDeclaration.makeAST(tokens.subList(0, assignIndex)) as Declaration
        val value : Value = scanValue.makeValue(tokens.subList(assignIndex + 1, tokens.size))
        return CompoundAssignation(tokens[assignIndex].getPosition(), decl, value)
    }

    private fun simpleAssignation(tokens: List<Token>, assignIndex: Int): Statement =
        Assignation(tokens[assignIndex].getPosition(), getVariable(tokens[0]), scanValue.makeValue(tokens.subList(assignIndex+1, tokens.size)))


    private fun checkIfCompound(tokens: List<Token>, assignIndex: Int) =
        tokens.subList(0, assignIndex).size != 1

    private fun getVariable(token: Token): Variable =
        Variable(token.getValue(), token.getPosition())


    private fun checkFirstPartOfAssignation(tokens: List<Token>, assignIndex: Int) =
        checkDeclaration(tokens, assignIndex) || checkIdentifier(tokens.subList(0, assignIndex))

    private fun checkDeclaration(tokens: List<Token>, assignIndex: Int) =
        scanDeclaration.canHandle(tokens.subList(0, assignIndex))

    private fun findAssignIndex(tokens: List<Token>): Int {
        for (i in tokens.indices)
            if (tokens[i].getType() == TokenType.ASSIGNATION) return i
        return -1
    }

    private fun checkIdentifier(tokens: List<Token>): Boolean {
        return if (tokens.size != 1) {
            false
        } else {
            tokens[0].getType() == TokenType.IDENTIFIER
        }
    }

}
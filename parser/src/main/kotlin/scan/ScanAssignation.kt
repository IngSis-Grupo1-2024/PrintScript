package scan

import components.Token
import components.TokenType
import components.ast.AST
import components.ast.ASTInterface
import components.statement.Assignation
import components.statement.Statement
import components.statement.Variable
import javax.swing.plaf.basic.BasicGraphicsUtils

class ScanAssignation : ScanStatement {
    private val scanDeclaration = ScanDeclaration()
    private val scanValue = ScanValue()

    override fun canHandle(tokens: List<Token>): Boolean {
        if (tokens.size < 3) return false
        val assignIndex = findAssignIndex(tokens)
        if (assignIndex == -1) return false
        if (checkFirstPartOfAssignation(tokens, assignIndex))
            return scanValue.canHandle(tokens.subList(assignIndex + 1, tokens.size))
        return false
    }

    override fun makeAST(tokens: List<Token>): Statement {
        val assignIndex: Int = findAssignIndex(tokens)
        if (checkIfCompound(tokens, assignIndex)) return compoundAssignation(tokens, assignIndex)
        return simpleAssignation(tokens)
    }

    private fun simpleAssignation(tokens: List<Token>): Statement {
        Assignation<>(getVariable(tokens[0]), scanValue.makeAST(tokens.subList(tokens.size - 1, tokens.size)))

    }


    private fun checkIfCompound(tokens: List<Token>, assignIndex: Int) =
        tokens.subList(0, assignIndex).size != 1

    private fun transformIdent(tokens: List<Token>): Statement {
        if (tokens.size == 1) return getVariable(tokens[0])
        return scanDeclaration.makeAST(tokens)
    }

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
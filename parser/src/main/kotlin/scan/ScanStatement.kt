package scan

import components.Token
import components.TokenType
import components.ast.ASTInterface
import components.statement.Statement

interface ScanStatement {
    fun canHandle(tokens: List<Token>): Boolean
    fun makeAST(tokens: List<Token>): Statement
}
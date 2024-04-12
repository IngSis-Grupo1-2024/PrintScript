package scan

import components.Token
import components.statement.Statement

interface ScanStatement {
    fun canHandle(tokens: List<Token>): Boolean

    fun canHandleWODelimiter(tokens: List<Token>): Boolean

    fun makeAST(tokens: List<Token>): Statement
}

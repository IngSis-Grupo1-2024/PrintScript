package ingsis.parser.scan

import ingsis.components.Token
import ingsis.components.statement.Statement

interface ScanStatement {
    fun canHandle(tokens: List<Token>): Boolean

    fun canHandleWODelimiter(tokens: List<Token>): Boolean

    fun makeAST(tokens: List<Token>): Statement?
}

package scan

import components.statement.Statement
import result.Result

interface ScanStatement {
    fun canHandle(statement: Statement): Boolean

    fun analyze(statement: Statement): Result
}

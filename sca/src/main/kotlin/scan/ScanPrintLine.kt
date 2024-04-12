package scan

import components.statement.Statement
import components.statement.StatementType
import result.Result
import result.ValidResult

class ScanPrintLine : ScanStatement {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.PRINT_LINE
    }

    override fun analyze(statement: Statement): Result {
        return ValidResult()
    }
}

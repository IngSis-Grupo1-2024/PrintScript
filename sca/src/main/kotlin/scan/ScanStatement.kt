package scan

import components.statement.Statement
import ingsis.utils.ReadScaRulesFile
import result.Result

interface ScanStatement {
    fun canHandle(statement: Statement): Boolean

    fun analyze(statement: Statement, jsonReader: ReadScaRulesFile): Result
}

package ingsis.sca.scan

import ingsis.components.statement.Statement
import ingsis.sca.result.Result
import ingsis.utils.ReadScaRulesFile

interface ScanStatement {
    fun canHandle(statement: Statement): Boolean

    fun analyze(
        statement: Statement,
        jsonReader: ReadScaRulesFile,
    ): Result
}

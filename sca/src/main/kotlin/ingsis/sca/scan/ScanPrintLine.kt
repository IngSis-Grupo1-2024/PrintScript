package ingsis.sca.scan

import ingsis.components.TokenType
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.sca.result.Result
import ingsis.sca.scan.function.arguments.checkFunctionArguments
import ingsis.utils.ReadScaRulesFile

class ScanPrintLine(private val literalsAllowed: List<TokenType>) : ScanStatement {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.PRINT_LINE
    }

    override fun analyze(
        statement: Statement,
        jsonReader: ReadScaRulesFile,
    ): Result {
        return checkFunctionArguments(statement, jsonReader, literalsAllowed)
    }
}

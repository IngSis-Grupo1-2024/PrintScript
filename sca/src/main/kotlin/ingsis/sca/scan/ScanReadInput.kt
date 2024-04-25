package ingsis.sca.scan

import ingsis.components.TokenType
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.sca.result.Result
import ingsis.sca.scan.function.arguments.checkFunctionArguments
import ingsis.utils.ReadScaRulesFile

class ScanReadInput(private val literalsAllowed: ArrayList<TokenType>) : ScanStatement {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.ASSIGNATION_READ_INPUT ||
            statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION_READ_INPUT
    }

    override fun analyze(
        statement: Statement,
        jsonReader: ReadScaRulesFile,
    ): Result {
        return checkFunctionArguments(statement, jsonReader, literalsAllowed)
    }
}

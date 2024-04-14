package ingsis.sca.scan

import ingsis.components.statement.CompoundAssignation
import ingsis.components.statement.Declaration
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.sca.result.InvalidResult
import ingsis.sca.result.Result
import ingsis.sca.result.ValidResult
import ingsis.sca.scan.cases.CaseScanner
import ingsis.sca.scan.cases.ScanCamelCase
import ingsis.sca.scan.cases.ScanSnakeCase
import ingsis.utils.ReadScaRulesFile

class ScanIdentifierCase() :
    ScanStatement {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.DECLARATION ||
                statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION
    }

    override fun analyze(
        statement: Statement,
        jsonReader: ReadScaRulesFile,
    ): Result {
        val declaration: Statement
        declaration =
            if (statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION) {
                (statement as CompoundAssignation).getDeclaration()
            } else {
                (statement as Declaration)
            }

        // A can handle should be here
        val scanner: CaseScanner =
            when (jsonReader.getIdentifierFormat()) {
                "camel case" -> {
                    ScanCamelCase()
                }

                "snake case" -> {
                    ScanSnakeCase()
                }

                else -> {
                    ScanCamelCase()
                }
            }

        when (val result = scanner.checkIdentifierFormat(declaration)) {
            is InvalidResult -> {
                return InvalidResult(result.getPosition(), result.getMessage())
            }
        }
        return ValidResult()
    }
}

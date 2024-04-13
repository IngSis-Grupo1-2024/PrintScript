package scan

import components.statement.CompoundAssignation
import components.statement.Declaration
import components.statement.Statement
import components.statement.StatementType
import ingsis.utils.ReadScaRulesFile
import result.InvalidResult
import result.Result
import result.ValidResult
import scan.cases.CaseScanner
import scan.cases.ScanCamelCase

class ScanIdentifierCase(private val caseScanner: CaseScanner = ScanCamelCase()) :
    ScanStatement {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.DECLARATION || statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION
    }

    override fun analyze(statement: Statement, jsonReader: ReadScaRulesFile): Result {
        val declaration: Statement
        declaration = if (statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION) {
            (statement as CompoundAssignation).getDeclaration()
        } else {
            (statement as Declaration)
        }
        when (val result = caseScanner.checkIdentifierFormat(declaration)) {
            is InvalidResult -> {
                return InvalidResult(result.getPosition(), result.getMessage())
            }
        }
        return ValidResult()
    }
}
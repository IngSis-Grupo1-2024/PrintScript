package ingsis.formatter.scan

import ingsis.components.statement.*
import ingsis.formatter.spacesCounter.AssignationSpaces
import ingsis.formatter.utils.FormatterRule

class ScanReadEnv : ScanStatement {
    private val scanDeclaration = ScanDeclaration()

    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.ASSIGNATION_READ_ENV ||
            statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION_READ_ENV
    }

    override fun format(
        statement: Statement,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        when (statement) {
            is AssignationReadEnv -> {
                return buildAssignationString(statement.getVariable().getName(), statement.getVariableName(), ruleMap)
            }

            is CompoundAssignationReadEnv -> {
                return buildDeclarationString(
                    statement.getDeclaration(),
                    ruleMap,
                ) + buildAssignationString("", statement.getParam(), ruleMap)
            }
        }
        return ""
    }

    private fun buildDeclarationString(
        declaration: Declaration,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        val declarationFormat = scanDeclaration.format(declaration, ruleMap)
        return declarationFormat.substring(0, declarationFormat.length - 2)
    }

    private fun buildAssignationString(
        variableName: String,
        value: String,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        val assignationSpaces = AssignationSpaces(ruleMap)
        return variableName + assignationSpaces.getAssignationSpaces() + "=" + assignationSpaces.getAssignationSpaces() +
            "readEnv(\"${value}\")" + ";" + "\n"
    }
}

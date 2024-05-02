package ingsis.formatter.scan

import ingsis.components.statement.*
import ingsis.formatter.extractor.ValueExtractor
import ingsis.formatter.spacesCounter.AssignationSpaces
import ingsis.formatter.utils.FormatterRule

class ScanReadInput : ScanStatement {
    private val scanDeclaration = ScanDeclaration()

    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.ASSIGNATION_READ_INPUT ||
            statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION_READ_INPUT
    }

    override fun format(
        statement: Statement,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        when (statement) {
            is AssignationReadInput -> {
                return buildAssignationString(statement.getVariable().getName(), statement.getArgument(), ruleMap)
            }

            is CompoundAssignationReadInput -> {
                return buildDeclarationString(
                    statement.getDeclaration(),
                    ruleMap,
                ) + buildAssignationString("", statement.getArgument(), ruleMap)
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
        value: Value,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        val valueExtractor = ValueExtractor(value)
        val assignationSpaces = AssignationSpaces(ruleMap)
        return variableName + assignationSpaces.getAssignationSpaces() + "=" + assignationSpaces.getAssignationSpaces() +
            "readInput(${valueExtractor.getValue()})" + ";" + "\n"
    }
}

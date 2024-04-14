package ingsis.formatter.scan

import ingsis.components.statement.*
import ingsis.formatter.extractor.ValueExtractor
import ingsis.formatter.spacesCounter.AssignationSpaces
import ingsis.formatter.utils.FormatterRule

class ScanCompoundAssignation : ScanStatement {
    private val scanDeclaration = ScanDeclaration()

    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION
    }

    override fun format(
        statement: Statement,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        val compoundAssignation = statement as CompoundAssignation
        return buildDeclarationString(compoundAssignation.getDeclaration(), ruleMap) +
            buildAssignationString(
                compoundAssignation.getValue(),
                ruleMap,
            )
    }

    private fun buildDeclarationString(
        declaration: Declaration,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        val declarationFormat = scanDeclaration.format(declaration, ruleMap)
        return declarationFormat.substring(0, declarationFormat.length - 2)
    }

    private fun buildAssignationString(
        value: Value,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        val valueExtractor = ValueExtractor(value)
        val assignationSpaces = AssignationSpaces(ruleMap)
        return assignationSpaces.getAssignationSpaces() + "=" + assignationSpaces.getAssignationSpaces() +
            valueExtractor.getValue() + ";" + "\n"
    }
}

package scan

import components.statement.*
import extractor.ValueExtractor
import spaces_counter.AssignationSpaces
import spaces_counter.DeclarationSpaces
import utils.FormatterRule

class ScanCompoundAssignation : ScanStatement {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION
    }

    override fun format(statement: Statement, ruleMap: Map<String, FormatterRule>): String {
        val compoundAssignation = statement as CompoundAssignation
        return buildDeclarationString(compoundAssignation.getDeclaration(), ruleMap) + buildAssignationString(
            compoundAssignation.getValue(),
            ruleMap
        )

    }

    private fun buildDeclarationString(declaration: Declaration, ruleMap: Map<String, FormatterRule>): String {
        val declarationSpaces = DeclarationSpaces(ruleMap)

        return declaration.getKeyword()
            .getValue() + " "  + declaration.getVariable().getName() + declarationSpaces.getBeforeDeclarationSpaces() +
                ":" + declarationSpaces.getAfterDeclarationSpaces() + declaration.getType()
            .getValue()
    }

    private fun buildAssignationString(
        value: Value,
        ruleMap: Map<String, FormatterRule>
    ): String {
        val valueExtractor = ValueExtractor(value)
        val assignationSpaces = AssignationSpaces(ruleMap)
        return assignationSpaces.getAssignationSpaces() + "=" + assignationSpaces.getAssignationSpaces() + valueExtractor.getValue() + ";" + "\n"
    }

}
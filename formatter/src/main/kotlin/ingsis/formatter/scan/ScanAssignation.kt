package ingsis.formatter.scan

import ingsis.components.statement.Assignation
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.formatter.extractor.ValueExtractor
import ingsis.formatter.spacesCounter.AssignationSpaces
import ingsis.formatter.utils.FormatterRule

class ScanAssignation : ScanStatement {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.ASSIGNATION
    }

    override fun format(
        statement: Statement,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        val assignation = statement as Assignation
        val variableName = assignation.getVariable().getName()
        val value = assignation.getValue()
        val valueExtractor = ValueExtractor(value)
        val assignationSpaces = AssignationSpaces(ruleMap)
        val spacesBetweenAssignation = assignationSpaces.getAssignationSpaces()
        return buildAssignationString(
            variableName,
            spacesBetweenAssignation,
            valueExtractor.getValue(),
        )
    }

    private fun buildAssignationString(
        variableName: String,
        spacesBetweenAssignation: String,
        valueAsString: String,
    ): String {
        return "$variableName$spacesBetweenAssignation=$spacesBetweenAssignation$valueAsString;\n"
    }
}

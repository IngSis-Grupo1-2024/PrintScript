package ingsis.components.statement

import ingsis.components.Position

class AssignationReadInput(private val position: Position, private val variable: Variable) : Statement {
    override fun getStatementType(): StatementType = StatementType.ASSIGNATION_READ_INPUT

    fun getVariable(): Variable = variable

    fun getPosition(): Position = position
}

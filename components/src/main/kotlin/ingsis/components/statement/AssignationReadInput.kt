package ingsis.components.statement

import ingsis.components.Position

class AssignationReadInput(
    private val position: Position,
    private val variable: Variable,
    private val argument: Value,
) : Statement {
    override fun getStatementType(): StatementType = StatementType.ASSIGNATION_READ_INPUT

    fun getVariable(): Variable = variable

    fun getPosition(): Position = position

    fun getArgument(): Value = argument

    override fun toString(): String {
        return "AssignationReadInput(variable=$variable, argument=$argument)"
    }
}

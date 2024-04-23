package ingsis.components.statement

import ingsis.components.Position

class AssignationReadEnv(
    private val position: Position,
    private val variable: Variable,
    private val envVariableName: String,
) : Statement {
    override fun getStatementType(): StatementType {
        return StatementType.ASSIGNATION_READ_ENV
    }

    fun getVariable(): Variable = variable

    fun getVariableName(): String = envVariableName

    fun getPosition(): Position = position

    override fun toString(): String {
        return "AssignationReadEnv(variable=$variable, envVariableName='$envVariableName')"
    }
}

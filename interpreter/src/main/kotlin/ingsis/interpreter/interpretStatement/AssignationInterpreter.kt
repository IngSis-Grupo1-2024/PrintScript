package ingsis.interpreter.interpretStatement

import components.statement.*
import ingsis.utils.Result
import ingsis.utils.evaluateExpression

class AssignationInterpreter : StatementInterpreter {
    override fun canHandle(statement: Statement): Boolean = statement.getStatementType() == StatementType.ASSIGNATION

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        val assignation = statement as Assignation
        val variable = assignation.getVariable()
        val value = assignation.getValue()

        val result = evaluateExpression(value, getVariableType(variable.getName(), previousState))
        previousState[variable.getName()] = Result(getVariableType(variable.getName(), previousState), result)

        return previousState
    }

    private fun getVariableType(
        variableName: String,
        previousState: Map<String, Result>,
    ): Type {
        return previousState[variableName]!!.getType()
    }
}

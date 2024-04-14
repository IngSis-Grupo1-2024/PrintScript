package ingsis.interpreter.interpretStatement

import components.statement.*
import ingsis.utils.*
import ingsis.utils.Result

class AssignationInterpreter : StatementInterpreter {
    private val functions = InterpreterFunctions()

    override fun canHandle(statement: Statement): Boolean = statement.getStatementType() == StatementType.ASSIGNATION

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        val assignation = statement as Assignation
        val variable = assignation.getVariable()
        val value = assignation.getValue()

        val result = functions.evaluateExpression(value, previousState)
        previousState[variable.getName()] = Result(functions.getVariableType(variable.getName(), previousState), result)

        return previousState
    }
}

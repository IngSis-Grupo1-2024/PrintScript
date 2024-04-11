package ingsis.interpreter.interpretStatement

import components.TokenType
import components.statement.*
import ingsis.utils.Result
import ingsis.utils.*

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

        val result = functions.evaluateExpression(value, functions.getVariableType(variable.getName(), previousState), previousState)
        previousState[variable.getName()] = Result(functions.getVariableType(variable.getName(), previousState), result)

        return previousState
    }

}

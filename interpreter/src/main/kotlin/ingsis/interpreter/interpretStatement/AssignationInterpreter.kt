package ingsis.interpreter.interpretStatement

import components.statement.Assignation
import components.statement.Operator
import components.statement.Statement
import components.statement.Type
import ingsis.utils.Result
import ingsis.utils.evaluateExpression

class AssignationInterpreter : StatementInterpreter {
    override fun canHandle(statement: Statement): Boolean {
        return statement is Assignation
    }

    override fun interpret(statement: Statement, previousState: HashMap<String, Result>): HashMap<String, Result> {
        val assignation = statement as Assignation
        val variable = assignation.getVariable()
        val value = assignation.getValue()

        val result = evaluateExpression(value, getVariableType(variable.getName(), previousState))
        previousState[variable.getName()] = Result(getVariableType(variable.getName(), previousState), result)

        return previousState
    }

    private fun getVariableType(variableName: String, previousState: Map<String, Result>): Type {
        return previousState[variableName]!!.getType()
    }


}
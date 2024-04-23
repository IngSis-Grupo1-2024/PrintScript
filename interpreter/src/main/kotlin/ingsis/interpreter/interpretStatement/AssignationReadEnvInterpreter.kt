package ingsis.interpreter.interpretStatement

import ingsis.components.statement.*
import ingsis.utils.Result
import ingsis.utils.checkIfNewValueTypeMatchesType
import ingsis.utils.checkMutability

class AssignationReadEnvInterpreter : StatementInterpreter {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.ASSIGNATION_READ_ENV
    }

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        statement as AssignationReadEnv
        val variable = statement.getVariable()
        val variableName = statement.getVariableName()
        val envValue = System.getenv(variableName)
        if (checkMutability(variable, previousState)) {
            val variableType = previousState[variable.getName()]?.getType()
            val variableModifier = previousState[variable.getName()]?.getModifier()
            val result = Result(variableType!!, variableModifier, envValue)
            if (checkIfNewValueTypeMatchesType(variable, result, previousState)) {
                previousState[variable.getName()] = result
            } else {
                throw IllegalArgumentException("Variable ${variable.getName()} not declared")
            }
        } else {
            throw IllegalArgumentException("You can't update the value of an immutable variable")
        }
        return previousState
    }
}

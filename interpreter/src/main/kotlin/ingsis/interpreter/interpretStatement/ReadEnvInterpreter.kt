package ingsis.interpreter.interpretStatement

import ingsis.components.statement.*
import ingsis.components.statement.Function
import ingsis.interpreter.environment.Environment
import ingsis.utils.Result

class ReadEnvInterpreter(private val environment: Environment) : StatementInterpreter {

    override fun canHandle(statement: Statement): Boolean = statement.getStatementType() == StatementType.READ_ENV

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {

        val readEnv = statement as ReadEnv
        val callingStatement = readEnv.getCallingStatement()
        val readEnvVariableName = readEnv.getVariableName()
        val value = environment.getVariable(readEnvVariableName)

        when (callingStatement) {
            is CompoundAssignation -> {
                val variableName = callingStatement.getDeclaration().getVariable().getName()
                val variableType = callingStatement.getDeclaration().getType()
                val variableModifier = callingStatement.getDeclaration().getKeyword().getModifier()
                previousState[variableName] = Result(variableType, variableModifier, value.toString())
            }
            is Assignation -> {
                val variableName = callingStatement.getVariable().getName()
                val variableType = previousState[variableName]!!.getType()
                val variableModifier = previousState[variableName]!!.getModifier()
                previousState[variableName] = Result(variableType, variableModifier)
            }
            is PrintLine -> {
//                val interpreter = PrintLineInterpreter(emptyList(), PrintOutputEmitter())
            }
        }

        return previousState
    }
}
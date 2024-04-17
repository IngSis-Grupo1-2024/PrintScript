package ingsis.interpreter.interpretStatement

import ingsis.components.statement.ReadEnv
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.components.statement.Type
import ingsis.interpreter.Environment
import ingsis.interpreter.EnvironmentVariable
import ingsis.utils.Result

class ReadEnvInterpreter(private val environment: Environment): StatementInterpreter {

    override fun canHandle(statement: Statement): Boolean = statement.getStatementType() == StatementType.READ_ENV

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): Pair<HashMap<String, Result>, String?> {

        val readEnv = statement as ReadEnv
        val variableName = readEnv.getVariableName()
        val value = environment.getVariable(variableName)

        return Pair(previousState, null)

    }
}
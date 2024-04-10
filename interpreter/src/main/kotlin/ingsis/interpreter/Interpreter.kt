package ingsis.interpreter

import components.statement.Statement
import ingsis.interpreter.interpretStatement.StatementInterpreter
import ingsis.utils.Result

class Interpreter(private val interpreters: List<StatementInterpreter>) {
    fun interpret(
        statement: Statement,
        variableMap: HashMap<String, Result>,
    ): HashMap<String, Result> {
        interpreters.forEach {
            if (it.canHandle(statement)) return it.interpret(statement, variableMap)
        }

        throw IllegalArgumentException("No interpreter found for statement: $statement")
    }
}

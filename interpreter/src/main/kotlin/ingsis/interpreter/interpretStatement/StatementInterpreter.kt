package ingsis.interpreter.interpretStatement

import components.statement.Statement
import ingsis.utils.Result

interface StatementInterpreter {
    fun canHandle(statement: Statement): Boolean

    fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result>
}

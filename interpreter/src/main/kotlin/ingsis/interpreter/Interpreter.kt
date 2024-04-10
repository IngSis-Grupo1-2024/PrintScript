package ingsis.interpreter

import components.statement.Statement
import ingsis.interpreter.interpretStatement.StatementInterpreter
import ingsis.utils.Result

class Interpreter (private val interpreters : List<StatementInterpreter>) {

    private val variableMap = HashMap<String, Result>()

    fun interpret(statement: Statement, previousState: HashMap<String, Result>): HashMap<String, Result> {
        for (interpreter in interpreters) {
            if (interpreter.canHandle(statement)) {
                return interpreter.interpret(statement, variableMap)
            }
        }
        throw IllegalArgumentException("No interpreter found for statement: $statement")
    }
}
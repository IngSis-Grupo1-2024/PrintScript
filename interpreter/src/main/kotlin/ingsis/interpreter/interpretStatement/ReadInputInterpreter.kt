package ingsis.interpreter.interpretStatement

import ingsis.components.statement.Function
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.utils.Result

class ReadInputInterpreter(private val input: Input) : StatementInterpreter {

    override fun canHandle(statement: Statement): Boolean {
        statement as Function
        return statement.getStatementType() == StatementType.FUNCTION && statement.getToken().getValue() == "readInput"
    }

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>
    ): HashMap<String, Result> {
        statement as Function
        input.readInput()
        return previousState
    }


}
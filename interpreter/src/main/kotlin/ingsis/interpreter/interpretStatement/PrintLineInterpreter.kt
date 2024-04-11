package ingsis.interpreter.interpretStatement

import components.Position
import components.TokenType
import components.statement.*
import ingsis.utils.InterpreterFunctions
import ingsis.utils.Result

class PrintLineInterpreter : StatementInterpreter {
    private val functions = InterpreterFunctions()

    override fun canHandle(statement: Statement): Boolean = statement.getStatementType() == StatementType.PRINT_LINE

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        val printLine = statement as PrintLine
        val valueToken = printLine.getValue().getToken()

        if (valueToken.getType() == TokenType.IDENTIFIER) {
            val variable = previousState[valueToken.getValue()]
            if (variable != null) {
                println(variable.getValue())
            }
        }
        else if (printLine.getValue() is Operator) {
            println(functions.evaluateExpression(printLine.getValue(), Type(valueToken.getValue(), Position()), previousState))
        }
        else {
            println(functions.evaluateExpression(printLine.getValue(), Type(valueToken.getValue(), Position()), previousState))
        }

        return previousState
    }
}

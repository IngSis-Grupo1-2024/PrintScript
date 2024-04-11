package ingsis.interpreter.interpretStatement

import components.Position
import components.Token
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
        val result : String = getResult(valueToken, previousState, printLine)

        println(result)

        return previousState
    }

    private fun getResult(
        valueToken: Token,
        previousState: HashMap<String, Result>,
        printLine: PrintLine
    ): String {
        val result : String
        if (valueToken.getType() == TokenType.IDENTIFIER) {
            val variable = previousState[valueToken.getValue()]
            result = variable?.getValue() ?: ""
        } else if (printLine.getValue() is Operator) {
            result = functions.evaluateExpression(
                printLine.getValue(),
                previousState
            )
        } else {
            result = functions.evaluateExpression(
                printLine.getValue(),
                previousState
            )
        }
        return result
    }
}

package ingsis.interpreter.interpretStatement

import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.PrintLine
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.interpreter.operatorScanner.ScanOperatorType
import ingsis.interpreter.valueAnalyzer.ValueAnalyzer
import ingsis.utils.Result

class PrintLineInterpreter(private val scanners: List<ScanOperatorType>) : StatementInterpreter {
//    private val functions = InterpreterFunctions()

    override fun canHandle(statement: Statement): Boolean = statement.getStatementType() == StatementType.PRINT_LINE

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        val printLine = statement as PrintLine
        val valueToken = printLine.getValue().getToken()
        val result: String = getResult(valueToken, previousState, printLine)

        println(result)

        return previousState
    }

    private fun getResult(
        valueToken: Token,
        previousState: HashMap<String, Result>,
        printLine: PrintLine,
    ): String {
        if (valueToken.getType() == TokenType.IDENTIFIER) {
            val variable = previousState[valueToken.getValue()]
            return variable?.getValue() ?: ""
        } else {
            val result = ValueAnalyzer(scanners).analyze(printLine.getValue(), previousState)
            return result.getValue()!!
        }

//            result =
//                functions.evaluateExpression(
//                    printLine.getValue(),
//                    previousState,
//                )
//        } else {
//            result =
//                functions.evaluateExpression(
//                    printLine.getValue(),
//                    previousState,
//                )
//        }
//        return result
    }
}

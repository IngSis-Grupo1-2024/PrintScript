package ingsis.interpreter.interpretStatement

import components.Token
import components.TokenType
import components.statement.PrintLine
import components.statement.Statement
import components.statement.StatementType
import ingsis.utils.Result
import scan.value.ScanOperatorType
import value.analyzer.ValueAnalyzer

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

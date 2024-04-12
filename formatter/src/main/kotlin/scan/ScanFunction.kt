package scan

import components.statement.Function
import components.statement.Statement
import components.statement.StatementType
import extractor.ValueExtractor
import new.line.counter.PrintNewLines
import utils.FormatterRule

class ScanFunction : ScanStatement {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.FUNCTION
    }

    override fun format(
        statement: Statement,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        val function = statement as Function
        val functionName = function.getToken().getValue()
        val value = ValueExtractor(function.getValue()).getValue()
        val printNewLines = PrintNewLines(ruleMap).getPrintlnLines()
        return buildFunctionString(functionName, value, printNewLines)
    }

    private fun buildFunctionString(
        functionName: String,
        value: String,
        printLines: String,
    ): String {
        return printLines + functionName + "(" + value + ")" + "\n"
    }
}

package ingsis.formatter.scan

import ingsis.components.statement.PrintLine
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.formatter.extractor.ValueExtractor
import ingsis.formatter.newLineCounter.PrintNewLines
import ingsis.formatter.utils.FormatterRule

class ScanPrintLine : ScanStatement {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.PRINT_LINE
    }

    override fun format(
        statement: Statement,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        val function = statement as PrintLine
        val value = ValueExtractor(function.getValue()).getValue()
        val printNewLines = PrintNewLines(ruleMap).getPrintlnLines()
        return buildFunctionString(value, printNewLines)
    }

    private fun buildFunctionString(
        value: String,
        printLines: String,
    ): String {
        return printLines + "println($value);\n"
    }
}

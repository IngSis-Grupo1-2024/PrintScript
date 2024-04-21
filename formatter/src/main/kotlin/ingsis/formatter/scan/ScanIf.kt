package ingsis.formatter.scan

import ingsis.components.statement.If
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.formatter.PrintScriptFormatter
import ingsis.formatter.tabsCounter.IfTabs
import ingsis.formatter.utils.FormatterRule

class ScanIf : ScanStatement {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.IF
    }

    override fun format(
        statement: Statement,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        statement as If
        val tabs = IfTabs().getTabs(ruleMap)
        return buildIfString(statement.getIfBlock(), tabs, ruleMap)
    }

    private fun buildIfString(
        ifBlock: List<Statement>,
        tabs: String,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        val formatter = PrintScriptFormatter.createFormatter("VERSION_2")
        var string = "if{\n"
        for (statement in ifBlock) {
            // We have to check if it is a print line because the print line functions in the formatting have '\n' in the
            // start so I have to put the tabs after the '\n'
            string +=
                if (statement.getStatementType() == StatementType.PRINT_LINE) {
                    addTabsToPrintLine(statement, ruleMap, tabs)
                } else {
                    tabs + formatter.format(statement, ruleMap)
                }
        }
        string += "}"
        return string
    }

    private fun addTabsToPrintLine(
        statement: Statement,
        ruleMap: Map<String, FormatterRule>,
        tabs: String,
    ): String {
        val formatter = PrintScriptFormatter.createFormatter("VERSION_2")
        val formattedString = formatter.format(statement, ruleMap)
        val newLineIndex = formattedString.indexOfFirst { it != '\n' }
        val newLines = formattedString.substring(0, newLineIndex)
        val stringWONewLines = formattedString.substring(newLineIndex)
        return newLines + tabs + stringWONewLines
    }
}

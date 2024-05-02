package ingsis.formatter.scan

import ingsis.components.statement.If
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.formatter.Formatter
import ingsis.formatter.PrintScriptFormatter
import ingsis.formatter.extractor.ValueExtractor
import ingsis.formatter.tabsCounter.IfTabs
import ingsis.formatter.utils.FormatterRule

class ScanIf : ScanStatement {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.IF || statement.getStatementType() == StatementType.ELSE
    }

    override fun format(
        statement: Statement,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        val tabs = IfTabs().getTabs(ruleMap)
        when (statement) {
            is If -> {
                return buildBlockWithCondition(statement, tabs, ruleMap)
            }
        }
        return ""
    }

    private fun buildBlockWithCondition(
        ifStatement: If,
        tabs: String,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        val value = ValueExtractor(ifStatement.getComparison()).getValue()
        val formatter = PrintScriptFormatter.createFormatter("VERSION_2")
        var string = "if($value){\n"
        string += formatBlock(ifStatement.getIfBlock(), ruleMap, tabs, formatter)
        string += "}"
        if (ifStatement.getElseStatement().isNotEmpty()) {
            return string + buildBlockWithoutCondition(ifStatement.getElseStatement(), tabs, ruleMap, "else")
        }
        return string + "\n"
    }

    private fun buildBlockWithoutCondition(
        ifBlock: List<Statement>,
        tabs: String,
        ruleMap: Map<String, FormatterRule>,
        type: String,
    ): String {
        val formatter = PrintScriptFormatter.createFormatter("VERSION_2")
        var string = " $type{\n"
        string += formatBlock(ifBlock, ruleMap, tabs, formatter)
        string += "}\n"
        return string
    }

    private fun formatBlock(
        ifBlock: List<Statement>,
        ruleMap: Map<String, FormatterRule>,
        tabs: String,
        formatter: Formatter,
    ): String {
        var string1 = ""
        for (statement in ifBlock) {
            // We have to check if it is a print line because the print line functions in the formatting have '\n' in the
            // start so I have to put the tabs after the '\n'
            string1 +=
                if (statement.getStatementType() == StatementType.PRINT_LINE) {
                    addTabsToPrintLine(statement, ruleMap, tabs, formatter)
                } else {
                    tabs + formatter.format(statement, ruleMap)
                }
        }
        return string1
    }

    private fun addTabsToPrintLine(
        statement: Statement,
        ruleMap: Map<String, FormatterRule>,
        tabs: String,
        formatter: Formatter,
    ): String {
        val formattedString = formatter.format(statement, ruleMap)
        val newLineIndex = formattedString.indexOfFirst { it != '\n' }
        val newLines = formattedString.substring(0, newLineIndex)
        val stringWONewLines = formattedString.substring(newLineIndex)
        return newLines + tabs + stringWONewLines
    }
}

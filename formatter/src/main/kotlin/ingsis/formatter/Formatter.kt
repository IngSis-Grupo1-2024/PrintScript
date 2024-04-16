package ingsis.formatter
import ingsis.components.statement.Statement
import ingsis.formatter.scan.*
import ingsis.formatter.utils.FormatterRule
import ingsis.formatter.utils.readJsonAndStackMap

object PrintScriptFormatter {
    fun createFormatter(version: String): Formatter {
        return when (version) {
            "VERSION_1" -> Formatter(listOf(ScanDeclaration(), ScanAssignation(), ScanPrintLine(), ScanCompoundAssignation()))
            else -> Formatter(listOf(ScanDeclaration(), ScanAssignation(), ScanPrintLine(), ScanCompoundAssignation()))
        }
    }
}

class Formatter(private val scanners: List<ScanStatement>) {
    fun format(
        statement: Statement,
        rulePath: String,
    ): String {
        var ruleMap: Map<String, FormatterRule> = mapOf()
        try {
            ruleMap = readJsonAndStackMap(rulePath)
        } catch (
            e: Exception,
        ) {
            println("Error reading the rules file")
        }
        val result = StringBuilder()
        for (scanner in scanners) {
            if (scanner.canHandle(statement)) {
                result.append(scanner.format(statement, ruleMap))
            }
        }
        return result.toString()
    }
}

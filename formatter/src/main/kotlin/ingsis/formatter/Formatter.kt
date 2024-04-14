package ingsis.formatter
import components.statement.Statement
import ingsis.formatter.scan.*
import ingsis.formatter.utils.readJsonAndStackMap

object PrintScriptFormatter {
    fun createFormatter(version: String): Formatter {
        return when (version) {
            "VERSION_1" -> Formatter(listOf(ScanDeclaration(), ScanAssignation(), ScanFunction(), ScanCompoundAssignation()))
            else -> Formatter(listOf(ScanDeclaration(), ScanAssignation(), ScanFunction(), ScanCompoundAssignation()))
        }
    }
}

class Formatter(private val scanners: List<ScanStatement>) {
    fun format(statement: Statement): String {
        val ruleMap = readJsonAndStackMap("src/main/rules/rules.json")
        val result = StringBuilder()
        for (scanner in scanners) {
            if (scanner.canHandle(statement)) {
                result.append(scanner.format(statement, ruleMap))
            }
        }
        return result.toString()
    }
}

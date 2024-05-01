package ingsis.formatter
import ingsis.components.statement.Statement
import ingsis.formatter.scan.*
import ingsis.formatter.utils.FormatterRule

object PrintScriptFormatter {
    fun createFormatter(version: String): Formatter {
        return when (version) {
            "VERSION_1" -> Formatter(listOf(ScanDeclaration(), ScanAssignation(), ScanPrintLine(), ScanCompoundAssignation()))
            "VERSION_2" -> Formatter(
                listOf(
                    ScanAssignation(),
                    ScanCompoundAssignation(),
                    ScanDeclaration(),
                    ScanIf(),
                    ScanPrintLine(),
                    ScanReadEnv(),
                    ScanReadInput(),
                ))

            else -> Formatter(listOf(ScanDeclaration(), ScanAssignation(), ScanPrintLine(), ScanCompoundAssignation()))
        }
    }
}

class Formatter(private val scanners: List<ScanStatement>) {
    fun format(
        statement: Statement,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        val result = StringBuilder()
        for (scanner in scanners) {
            if (scanner.canHandle(statement)) {
                result.append(scanner.format(statement, ruleMap))
            }
        }
        return result.toString()
    }
}
